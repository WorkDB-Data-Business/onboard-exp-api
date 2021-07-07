package br.com.harvest.onboardexperience.domain.exception;

import br.com.harvest.onboardexperience.domain.enumerators.ExceptionEnumInterface;
import br.com.harvest.onboardexperience.domain.exception.enumerators.FactoryExceptionEnum;
import lombok.Getter;

@Getter
public class FactoryException extends RuntimeException {

	private ExceptionEnumInterface exception;
	
	private static final long serialVersionUID = 2841627938904433841L;
	
	public FactoryException(String message) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD.getValue() + ": " + message);
	}
	
	public FactoryException(String message, Throwable cause) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD.getValue() + ": " + message, cause);
	}
	
	public FactoryException(String message, String customCause) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD.getValue() + ": " + message);
	}
	
	public FactoryException(ExceptionEnumInterface exception) {
		super(FactoryExceptionEnum.ERROR_FACTORY_BUILD.getValue() + ": " + exception.getValue());
	}
	
}
