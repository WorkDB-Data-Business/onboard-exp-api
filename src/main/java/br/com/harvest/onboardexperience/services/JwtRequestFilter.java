package br.com.harvest.onboardexperience.services;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import br.com.harvest.onboardexperience.domain.exceptions.TenantForbiddenException;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtils jwtTokenUtil;
	
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("Authorization");

		String email = null;
		String jwtToken = null;
		
		if (ObjectUtils.isNotEmpty(requestTokenHeader) && !requestTokenHeader.contains("null") && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			
			final String subdomain = GenericUtils.getSubDomainOrThrownException(request, response, resolver);
			
			if(!subdomain.equalsIgnoreCase(jwtTokenUtil.getUsernameTenant(jwtToken))) {
				resolver.resolveException(request, response, null, new TenantForbiddenException("The tenant from token and from subdomain is different.",
						new Throwable("Access another tenant is forbidden.")));
				return;
			}
			
			try {
				email = jwtTokenUtil.getEmailFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				log.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				log.error("JWT Token has expired");
			}
			
		} else {
			log.warn("JWT Token does not begin with Bearer String");
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(email);

			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}


}