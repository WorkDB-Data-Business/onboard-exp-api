package br.com.harvest.onboardexperience.domain.exceptions;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.UserExceptionEnum;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 4628899633610403071L;
	
	private ExceptionEnumInterface exception;

	public UserNotFoundException(String message) {
		super(UserExceptionEnum.USER_NOT_FOUND.getValue() + ": " + message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(UserExceptionEnum.USER_NOT_FOUND.getValue() + ": " + message, cause);
	}

	public UserNotFoundException(String message, String customCause) {
		super(UserExceptionEnum.USER_NOT_FOUND.getValue() + ": " +  message);
	}	

}
