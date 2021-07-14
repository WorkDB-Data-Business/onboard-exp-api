package br.com.harvest.onboardexperience.domain.exceptions;

import br.com.harvest.onboardexperience.domain.exceptions.enumerators.FactoryExceptionEnum;

public class FactoryException extends RuntimeException {
	
	private static final long serialVersionUID = 2841627938904433841L;
	
	
	public FactoryException(String message) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD.getValue() + ": " + message);
	}
	
	public FactoryException(String message, Throwable cause) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD.getValue()  + ": " + message, cause);
	}
	
	public FactoryException(String message, String customCause) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD.getValue()  + ": " +  message);
	}
	
}
