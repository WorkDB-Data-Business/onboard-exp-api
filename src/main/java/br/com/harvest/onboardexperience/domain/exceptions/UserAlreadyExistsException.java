package br.com.harvest.onboardexperience.domain.exceptions;

import br.com.harvest.onboardexperience.domain.exceptions.enumerators.UserExceptionEnum;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -5029904193738626112L;

	public UserAlreadyExistsException(String message) {
		super(UserExceptionEnum.USER_ALREADY_EXISTS.getValue() + ": " + message);
	}

	public UserAlreadyExistsException(String message, Throwable cause) {
		super(UserExceptionEnum.USER_ALREADY_EXISTS.getValue() + ": " + message, cause);
	}

	public UserAlreadyExistsException(String message, String customCause) {
		super(UserExceptionEnum.USER_ALREADY_EXISTS.getValue() + ": " +  message);
	}
	
}
