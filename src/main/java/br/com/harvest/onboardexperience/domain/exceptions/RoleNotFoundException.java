package br.com.harvest.onboardexperience.domain.exceptions;

public class RoleNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -775382390514998188L;

	private static final String ROLE_NOT_FOUND = "Role not found";
	
	public RoleNotFoundException(String message) {
		super(ROLE_NOT_FOUND + ": " + message);
	}

	public RoleNotFoundException(String message, Throwable cause) {
		super(ROLE_NOT_FOUND + ": " + message, cause);
	}

	public RoleNotFoundException(String message, String customCause) {
		super(ROLE_NOT_FOUND + ": " +  message);
	}	

}
