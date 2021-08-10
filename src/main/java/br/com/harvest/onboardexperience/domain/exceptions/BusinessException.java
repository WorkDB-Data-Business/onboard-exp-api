package br.com.harvest.onboardexperience.domain.exceptions;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 3528466777307424165L;
	
	private static final String BUSINESS_ERROR = "This action breaks a business rule";

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause, false, true);
	}

	public BusinessException(String message, String customCause) {
		super(BUSINESS_ERROR + ": " +  message);
	}

}
