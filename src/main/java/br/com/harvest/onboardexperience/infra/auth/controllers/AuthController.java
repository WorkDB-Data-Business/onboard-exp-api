package br.com.harvest.onboardexperience.infra.auth.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.ChangePasswordForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.EmailForm;
import br.com.harvest.onboardexperience.domain.entities.PasswordResetToken;
import br.com.harvest.onboardexperience.usecases.UserUseCase;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerExceptionResolver;

import br.com.harvest.onboardexperience.infra.auth.dtos.JwtRequest;
import br.com.harvest.onboardexperience.infra.auth.dtos.JwtResponse;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.UserBlockedException;
import br.com.harvest.onboardexperience.domain.exceptions.UserDisabledException;
import br.com.harvest.onboardexperience.infra.auth.services.JwtUserDetailsService;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.usecases.LoginUseCase;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 36000)
public class AuthController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenUtils jwtUtil;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;
	
	@Autowired
	private LoginUseCase login;

	@Autowired
	private UserUseCase userUseCase;

	@Operation(description = "Realiza a autenticação e retorna o token JWT.")
	@RequestMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, TimeZone timeZone) {

		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		
		Boolean isFirstLogin = login.doFirstLoginByEmail(authenticationRequest.getEmail());
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getEmail());

		final String token = jwtUtil.generateToken(createUserClaims(userDetails, timeZone, isFirstLogin, authenticationRequest.getRememberMe()), userDetails, authenticationRequest.getRememberMe());

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	private void authenticate(String username, String password) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new UserDisabledException("The user is disabled", e);
		} catch (LockedException e) {
			throw new UserBlockedException("The user is blocked", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid credentials", e);
		}
	}
	
	private Map<String, Object> createUserClaims(UserDetails userDetails, TimeZone timeZone, Boolean isFirstLogin, Boolean rememberMe){
		User user = userService.findUserByEmail(userDetails.getUsername());
		Map<String, Object> claims = new HashMap<>();
		claims.put("user_id", user.getId());
		claims.put("user_tenant", user.getClient().getTenant());
		claims.put("user_time_zone", timeZone.getID());
		claims.put("user_first_login", isFirstLogin);
		claims.put("remember_me", rememberMe);
		
		return claims;
	}

	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "Envia o e-mail de 'esqueci minha senha'.")
	@PostMapping(value = "/auth/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
	public void sendEmailForgotPassword(@NotNull @Valid @RequestBody EmailForm form, HttpServletRequest request) throws Exception {
		userUseCase.sendEmailToResetPassword(form, request);
	}

	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "Altera a senha pelo token.")
	@PostMapping(value = "/auth/change-password/", produces = MediaType.APPLICATION_JSON_VALUE)
	public void changePassword(@RequestParam String token, @NotNull @Valid @RequestBody ChangePasswordForm form) {
		userUseCase.resetPassword(token, form);
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
}
