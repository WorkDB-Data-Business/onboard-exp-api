package br.com.harvest.onboardexperience.domain.exceptions;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.FactoryExceptionEnum;
import lombok.Getter;

@Getter
public class FactoryException extends RuntimeException {
	
	private static final long serialVersionUID = 2841627938904433841L;
	
	private ExceptionEnumInterface exception;
	
	public FactoryException(String message) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD + ": " + message);
	}
	
	public FactoryException(String message, Throwable cause) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD  + ": " + message, cause);
	}
	
	public FactoryException(String message, String customCause) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD  + ": " +  message);
	}
	
	public FactoryException(ExceptionEnumInterface exception) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD  + ": " + exception.getValue());
		this.exception = exception;
	}
	
}
