package br.com.harvest.onboardexperience.domain.exception.enumerators;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import lombok.Getter;

@Getter
public enum FactoryExceptionEnum implements ExceptionEnumInterface {

	ERROR_FACTORY_BUILD("FE01", "An error occurred at factory build", "Generic cause message", "error.factory.build", "error.factory.build_cause"),
	CURRENT_ERROR_NULL("FE02", "The current error cannot be null", "The current error is null", "error.factory.current_error_null", "error.factory.current_error_null_cause"),
	COMMENTS_CANNOT_BE_NULL("FE03", "Comments cannot be null", "Comments is null", "error.factory.comments_cannot_be_null", "error.factory.comments_cannot_be_null_cause"),
	MESSAGE_CANNOT_BE_NULL("FE04", "Message cannot be null", "Message is null", "error.factory.message_cannot_be_null", "error.factory.message_cannot_be_null_cause"),
	CAUSE_CANNOT_BE_NULL("FE05", "Cause cannot be null", "Cause is null", "error.factory.cause_cannot_be_null", "error.factory.cause_cannot_be_null_cause"),
	ERROR_CANNOT_BE_NULL("FE06", "Error cannot be null", "Error is null", "error.factory.error_cannot_be_null", "error.factory.error_cannot_be_null_cause"),
	ERRORS_CANNOT_BE_NULL("FE07", "Errors cannot be null", "Errors is null", "error.factory.errors_cannot_be_null", "error.factory.errors_cannot_be_null_cause"),
	GENERIC_ERROR_MESSAGE_FACTORY("FE08", "An error occurred at building return response message", "Generic cause message", "error.factory.generic_error_message_factory", "error.factory.generic_error_message_factory_cause"),
	MESSAGE_KEY_CANNOT_BE_NULL("FE09", "Message key cannot be null", "Message key is null", "error.factory.message_key_cannot_be_null", "error.factory.message_key_cannot_be_null_cause"),
	COMMENT_CANNOT_BE_NULL("FE10", "Comment cannot be null", "Comment is null", "error.factory.comment_cannot_be_null", "error.factory.comment_cannot_be_null_cause"),
	CAUSE_KEY_CANNOT_BE_NULL("FE11", "Cause key cannot be null", "Cause key is null", "error.factory.cause_key_cannot_be_null", "error.factory.cause_key_cannot_be_null_cause");
	
	private String code;
	private String value;
	private String cause;
	private String valueKey;
	private String causeKey;
	
	private FactoryExceptionEnum(final String code, final String value, final String cause, final String valueKey, final String causeKey) {
		this.code = code;
		this.value = value;
		this.cause = cause;
		this.valueKey = valueKey;
		this.causeKey = causeKey;
	}
	
	
	public static FactoryExceptionEnum valueOfCodigo(String codigo) {
		for (FactoryExceptionEnum error: FactoryExceptionEnum.values()) {
			if (error.getCode().equalsIgnoreCase(codigo)) {
				return error;
			}
		}
		return null;
	}
	
	public static FactoryExceptionEnum valueOfvalueKey(String valueKey) {
		for (FactoryExceptionEnum error: FactoryExceptionEnum.values()) {
			if (error.getValueKey().equals(valueKey)) {
				return error;
			}
		}
		return null;
	}

}
