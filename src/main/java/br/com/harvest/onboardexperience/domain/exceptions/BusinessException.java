package br.com.harvest.onboardexperience.domain.exceptions;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.BusinessExceptionEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 3528466777307424165L;

	private ExceptionEnumInterface exception;

	public BusinessException(String message) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " + message);
	}

	public BusinessException(String message, Throwable cause) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " + message, cause);
	}

	public BusinessException(String message, String customCause) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " +  message);
	}

	public BusinessException(ExceptionEnumInterface exception) {
		super(BusinessExceptionEnum.BUSINESS_GENERIC_ERROR + ": " + exception.getValue());
		this.exception = exception;
	}

}
