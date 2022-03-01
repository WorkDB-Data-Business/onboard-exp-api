package br.com.harvest.onboardexperience.domain.exceptions;

public class InvalidCpfException extends RuntimeException {

	private static final long serialVersionUID = -997130304356792004L;
	
	private static final String INVALID_CPF = "Invalid CPF";
	
	public InvalidCpfException(String message) {
		super(INVALID_CPF + ": " + message);
	}

	public InvalidCpfException(String message, Throwable cause) {
		super(INVALID_CPF + ": " + message, cause);
	}

	public InvalidCpfException(String message, String customCause) {
		super(INVALID_CPF + ": " +  message);
	}	

}
