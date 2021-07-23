package br.com.harvest.onboardexperience.domain.exceptions;

public class UserDisabledException extends RuntimeException {

	private static final long serialVersionUID = -4700722296158496912L;
	
	private static final String USER_DISABLED = "The user is disabled";

	public UserDisabledException(String message) {
		super(USER_DISABLED + ": " + message);
	}

	public UserDisabledException(String message, Throwable cause) {
		super(USER_DISABLED + ": " + message, cause);
	}

	public UserDisabledException(String message, String customCause) {
		super(USER_DISABLED + ": " +  message);
	}

}
