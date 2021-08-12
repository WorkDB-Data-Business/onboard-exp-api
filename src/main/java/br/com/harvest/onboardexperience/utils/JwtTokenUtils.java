package br.com.harvest.onboardexperience.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.com.harvest.onboardexperience.configurations.environment.EnvironmentVariable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtils implements Serializable {

	private static final long serialVersionUID = -7137341187394251979L;

	public static final long JWT_TOKEN_VALIDITY =  1000 * 1 * 60 * 60 ;
	public static final long JWT_TOKEN_VALIDITY_REMEMBER =  1000 * 24 * 7 * 60 * 60;
	
	@Autowired
	private EnvironmentVariable env;

	public String getEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public String getUserTimeZone(String token) {
		return getAllClaimsFromToken(extractTokenFromHeader(token)).get("user_time_zone", String.class);
	}
	
	public String getUserTenant(String token) {
		return getAllClaimsFromToken(extractTokenFromHeader(token)).get("user_tenant", String.class);
	}
	
	public Boolean getUserIsActive(String token) {
		return getAllClaimsFromToken(extractTokenFromHeader(token)).get("user_is_active", Boolean.class);
	}
	
	public Long getUserId(String token) {
		return getAllClaimsFromToken(extractTokenFromHeader(token)).get("user_id", Long.class);
	}
	
	public <T> T getAnyClaimFromToken(String token, String claim, Class<T> requiredType){
		return getAllClaimsFromToken(extractTokenFromHeader(token)).get(claim, requiredType);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	public <T> T extractTokenAndGetClaim(String token, String claim, Class<T> requiredType) {
		if(ObjectUtils.isNotEmpty(token) && token.startsWith("Bearer ")) {
			return getAnyClaimFromToken(token.substring(7), claim, requiredType);
		}
		return null;
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(env.getJwtSecret()).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(Map<String, Object> claims, UserDetails userDetails, Boolean rememberMe) {
		if(java.util.Objects.isNull(claims)) claims = new HashMap<>(); 
		
		return doGenerateToken(claims, userDetails.getUsername(), rememberMe);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject, Boolean rememberMe) {
		Date expiration;
		if(rememberMe){
			expiration = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_REMEMBER);
		}else{
			expiration = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);
		}
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS512, env.getJwtSecret()).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String email = getEmailFromToken(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String extractTokenFromHeader(String token) {
		if(ObjectUtils.isNotEmpty(token) && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return token;
	}

	public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
		var rememberMe = (Boolean)claims.get("remember_me");
		Date expiration;
		if(rememberMe){
			expiration = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_REMEMBER);
		}else{
			expiration = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);
		}

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS512, env.getJwtSecret()).compact();
	}
}
