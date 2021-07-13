package br.com.harvest.onboardexperience.domain.exceptions.enumerators;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import lombok.Getter;

@Getter
public enum ExceptionEnum implements ExceptionEnumInterface {
	
	INTERNAL_SERVER_GENERIC_ERROR("E01", "An internal server error ocurred", "Request support for help with this issue", "error.internal_server_generic_error", "error.internal_server_generic_error_cause"),
	GENERIC_EXCEPTION_MESSAGE("E02", "An exception has been thrown", "Verify the root cause", "error.generic_exception_message", "error.generic_exception_message_cause"),
	RECORD_NOT_FOUND("E03", "The record has been not found", "dsds", "error.record_not_found");
	
	private String code;
	private String value;
	private String cause;
	private String valueKey;
	private String causeKey;
	
	private ExceptionEnum(final String code, final String value, final String cause, final String valueKey, final String causeKey) {
		this.code = code;
		this.value = value;
		this.cause = cause;
		this.valueKey = valueKey;
		this.causeKey = causeKey;
	}
	
	
	public static ExceptionEnum valueOfCodigo(String codigo) {
		for (ExceptionEnum error: ExceptionEnum.values()) {
			if (error.getCode().equalsIgnoreCase(codigo)) {
				return error;
			}
		}
		return null;
	}
	
	public static ExceptionEnum valueOfValueKey(String valueKey) {
		for (ExceptionEnum error: ExceptionEnum.values()) {
			if (error.getValueKey().equals(valueKey)) {
				return error;
			}
		}
		return null;
	}
	
	public static ExceptionEnum valueOfCauseKey(String causeKey) {
		for (ExceptionEnum error: ExceptionEnum.values()) {
			if (error.getCauseKey().equals(causeKey)) {
				return error;
			}
		}
		return null;
	}


}
