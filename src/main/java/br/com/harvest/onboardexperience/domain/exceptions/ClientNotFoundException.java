package br.com.harvest.onboardexperience.domain.exceptions;

public class ClientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6594452227689779228L;
	
	private static final String CLIENT_NOT_FOUND = "Client not found";

	public ClientNotFoundException(String message) {
		super(CLIENT_NOT_FOUND + ": " + message);
	}

	public ClientNotFoundException(String message, Throwable cause) {
		super(CLIENT_NOT_FOUND + ": " + message, cause);
	}

	public ClientNotFoundException(String message, String customCause) {
		super(CLIENT_NOT_FOUND + ": " +  message);
	}	
	
	public static String buildMessage(String field, String value) {
		return "The client with " + field + " " + value + " not found"; 
	}

}
