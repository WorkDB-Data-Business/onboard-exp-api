package br.com.harvest.onboardexperience.domain.exceptions;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.BusinessExceptionEnum;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 4628899633610403071L;
	
	private ExceptionEnumInterface exception;

	public UserNotFoundException(String message) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " + message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " + message, cause);
	}

	public UserNotFoundException(String message, String customCause) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " +  message);
	}

	public UserNotFoundException(ExceptionEnumInterface exception) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " + exception.getValue());
		this.exception = exception;
	}

}
