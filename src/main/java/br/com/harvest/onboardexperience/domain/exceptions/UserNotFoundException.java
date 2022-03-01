package br.com.harvest.onboardexperience.domain.exceptions;

public class UserNotFoundException extends RuntimeException  {
	
	private static final long serialVersionUID = 4628899633610403071L;
	
	private static final String USER_NOT_FOUND = "User not found";

	public UserNotFoundException(String message) {
		super(USER_NOT_FOUND + ": " + message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(USER_NOT_FOUND + ": " + message, cause);
	}

	public UserNotFoundException(String message, String customCause) {
		super(USER_NOT_FOUND + ": " +  message);
	}

}
