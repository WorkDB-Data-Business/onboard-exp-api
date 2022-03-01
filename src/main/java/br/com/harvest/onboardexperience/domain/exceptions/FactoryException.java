package br.com.harvest.onboardexperience.domain.exceptions;

public class FactoryException extends RuntimeException {
	
	private static final long serialVersionUID = 2841627938904433841L;
	
	private static final String FACTORY_ERROR = "An error occurred at factory build";
	
	public FactoryException(String message) {
		super(FACTORY_ERROR + ": " + message);
	}
	
	public FactoryException(String message, Throwable cause) {
		super(FACTORY_ERROR + ": " + message, cause);
	}
	
	public FactoryException(String message, String customCause) {
		super(FACTORY_ERROR + ": " +  message);
	}
	
}
