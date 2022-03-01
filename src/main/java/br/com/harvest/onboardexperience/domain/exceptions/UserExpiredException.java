package br.com.harvest.onboardexperience.domain.exceptions;

public class UserExpiredException extends RuntimeException {

	private static final long serialVersionUID = -6521678243658478166L;
	
	private static final String USER_EXPIRED = "The user is expired";

	public UserExpiredException(String message) {
		super(USER_EXPIRED + ": " + message);
	}

	public UserExpiredException(String message, Throwable cause) {
		super(USER_EXPIRED + ": " + message, cause);
	}

	public UserExpiredException(String message, String customCause) {
		super(USER_EXPIRED + ": " +  message);
	}
	
}
