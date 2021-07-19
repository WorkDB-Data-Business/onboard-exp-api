package br.com.harvest.onboardexperience.domain.exceptions;

public class InvalidCnpjException extends RuntimeException {

	private static final long serialVersionUID = -997130304356792004L;
	
	private static final String INVALID_CNPJ = "Invalid CNPJ";
	
	public InvalidCnpjException(String message) {
		super(INVALID_CNPJ + ": " + message);
	}

	public InvalidCnpjException(String message, Throwable cause) {
		super(INVALID_CNPJ + ": " + message, cause);
	}

	public InvalidCnpjException(String message, String customCause) {
		super(INVALID_CNPJ + ": " +  message);
	}	
	
}
