package br.com.harvest.onboardexperience.domain.exceptions;

import org.springframework.security.core.AuthenticationException;

public class SubdomainNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 7838637065687055104L;

	private static final String SUBDOMAIN_NOT_FOUND = "Subdomain not found";
	
	public SubdomainNotFoundException(String message) {
		super(SUBDOMAIN_NOT_FOUND + ": " + message);
	}

	public SubdomainNotFoundException(String message, Throwable cause) {
		super(SUBDOMAIN_NOT_FOUND + ": " + message, cause);
	}

	public SubdomainNotFoundException(String message, String customCause) {
		super(SUBDOMAIN_NOT_FOUND + ": " +  message);
	}	
	
}
