package br.com.harvest.onboardexperience.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExceptionResolver;

import br.com.harvest.onboardexperience.domain.dto.JwtRequest;
import br.com.harvest.onboardexperience.domain.dto.JwtResponse;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.UserBlockedException;
import br.com.harvest.onboardexperience.domain.exceptions.UserDisabledException;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.services.JwtUserDetailsService;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/v1")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenUtils jwtUtil;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;

	@Operation(description = "Realiza a autenticação e retorna o token JWT.")
	@RequestMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletRequest request,HttpServletResponse 
			response, TimeZone timeZone) throws Exception {
		
		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getEmail());

		final String token = jwtUtil.generateToken(createUserClaims(userDetails, timeZone), userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	private void authenticate(String username, String password) throws Exception {
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
	
	private Map<String, Object> createUserClaims(UserDetails userDetails, TimeZone timeZone){
		User user = repository.findByEmailContainingIgnoreCase(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException("User with email " 
				+ userDetails.getUsername() + " not found."));
		Map<String, Object> claims = new HashMap<>();
		claims.put("user_id", user.getId());
		claims.put("user_tenant", user.getClient().getTenant());
		claims.put("user_time_zone", timeZone.getID());
		
		return claims;
	}

}
