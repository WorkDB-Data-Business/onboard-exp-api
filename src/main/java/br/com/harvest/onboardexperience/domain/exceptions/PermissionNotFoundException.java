package br.com.harvest.onboardexperience.domain.exceptions;

public class PermissionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3703713355410808283L;
	
	private static final String PERMISSION_NOT_FOUND = "Permission not found";
	
	public PermissionNotFoundException(String message) {
		super(PERMISSION_NOT_FOUND + ": " + message);
	}

	public PermissionNotFoundException(String message, Throwable cause) {
		super(PERMISSION_NOT_FOUND + ": " + message, cause);
	}

	public PermissionNotFoundException(String message, String customCause) {
		super(PERMISSION_NOT_FOUND + ": " +  message);
	}	


}
