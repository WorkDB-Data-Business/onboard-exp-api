package br.com.harvest.onboardexperience.domain.exceptions;

public class UserBlockedException extends RuntimeException {

	private static final long serialVersionUID = 4947091467591544745L;
	
	private static final String USER_BLOCKED = "The user is blocked";

	public UserBlockedException(String message) {
		super(USER_BLOCKED + ": " + message);
	}

	public UserBlockedException(String message, Throwable cause) {
		super(USER_BLOCKED + ": " + message, cause);
	}

	public UserBlockedException(String message, String customCause) {
		super(USER_BLOCKED + ": " +  message);
	}
	
}
