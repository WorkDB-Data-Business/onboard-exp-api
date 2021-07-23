package br.com.harvest.onboardexperience.domain.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TenantForbiddenException extends AuthenticationException {

	private static final long serialVersionUID = 3339127497437594182L;
	
	private static final String TENANT_FORBIDDEN_ACCESS = "Tenant forbidden access";
	
	public TenantForbiddenException(String message) {
		super(TENANT_FORBIDDEN_ACCESS + ": " + message);
	}

	public TenantForbiddenException(String message, Throwable cause) {
		super(TENANT_FORBIDDEN_ACCESS + ": " + message, cause);
	}

	public TenantForbiddenException(String message, String customCause) {
		super(TENANT_FORBIDDEN_ACCESS + ": " +  message);
	}	
	
}
