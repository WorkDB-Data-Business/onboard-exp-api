package br.com.harvest.onboardexperience.domain.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -5029904193738626112L;
	
	private static final String USER_ALREADY_EXISTS_ERROR = "The user already exists";

	public UserAlreadyExistsException(String message) {
		super(USER_ALREADY_EXISTS_ERROR + ": " + message);
	}

	public UserAlreadyExistsException(String message, Throwable cause) {
		super(USER_ALREADY_EXISTS_ERROR + ": " + message, cause);
	}

	public UserAlreadyExistsException(String message, String customCause) {
		super(USER_ALREADY_EXISTS_ERROR + ": " +  message);
	}
	
}
