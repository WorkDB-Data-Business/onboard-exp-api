package br.com.harvest.onboardexperience.domain.exceptions;

public class InvalidTokenFormatException extends RuntimeException {

	private static final long serialVersionUID = -3995163522372228642L;
	
	private static final String INVALID_TOKEN_FORMAT = "The format of token is invalid";
	
	public InvalidTokenFormatException(String message) {
		super(INVALID_TOKEN_FORMAT + ": " + message);
	}

	public InvalidTokenFormatException(String message, Throwable cause) {
		super(INVALID_TOKEN_FORMAT + ": " + message, cause);
	}

	public InvalidTokenFormatException(String message, String customCause) {
		super(INVALID_TOKEN_FORMAT + ": " +  message);
	}

}
