package br.com.harvest.onboardexperience.domain.exceptions;

public class ClientAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -2906099966462616146L;
	
	private static final String CLIENT_ALREADY_EXISTS_ERROR = "The client already exists";

	public ClientAlreadyExistsException(String message) {
		super(CLIENT_ALREADY_EXISTS_ERROR + ": " + message);
	}

	public ClientAlreadyExistsException(String message, Throwable cause) {
		super(CLIENT_ALREADY_EXISTS_ERROR + ": " + message, cause);
	}

	public ClientAlreadyExistsException(String message, String customCause) {
		super(CLIENT_ALREADY_EXISTS_ERROR + ": " +  message);
	}

}
