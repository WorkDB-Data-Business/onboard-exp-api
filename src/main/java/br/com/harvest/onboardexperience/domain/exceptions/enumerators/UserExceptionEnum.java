package br.com.harvest.onboardexperience.domain.exceptions.enumerators;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import lombok.Getter;

@Getter
public enum UserExceptionEnum implements ExceptionEnumInterface {
	
	USER_NOT_FOUND("E03", "The user has been not found", "Doesn't have any user in database with this field value", "error.user_not_found", "error.user_not_found_cause"),
	USER_NOT_FOUND_ID("E04", "The user with this ID has been not found", "Doesn't have any user in database with this field value", "error.user_not_found_id", "error.user_not_found_cause");

	private String code;
	private String value;
	private String cause;
	private String valueKey;
	private String causeKey;
	
	private UserExceptionEnum(final String code, final String value, final String cause, final String valueKey, final String causeKey) {
		this.code = code;
		this.value = value;
		this.cause = cause;
		this.valueKey = valueKey;
		this.causeKey = causeKey;
	}
	
	
	public static UserExceptionEnum valueOfCodigo(String codigo) {
		for (UserExceptionEnum error: UserExceptionEnum.values()) {
			if (error.getCode().equalsIgnoreCase(codigo)) {
				return error;
			}
		}
		return null;
	}
	
	public static UserExceptionEnum valueOfvalueKey(String valueKey) {
		for (UserExceptionEnum error: UserExceptionEnum.values()) {
			if (error.getValueKey().equals(valueKey)) {
				return error;
			}
		}
		return null;
	}

}
