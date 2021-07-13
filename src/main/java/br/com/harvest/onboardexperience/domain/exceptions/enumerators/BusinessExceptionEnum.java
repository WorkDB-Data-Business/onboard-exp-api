package br.com.harvest.onboardexperience.domain.exceptions.enumerators;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
import lombok.Getter;

@Getter
public enum BusinessExceptionEnum implements ExceptionEnumInterface {
	
	BUSINESS_GENERIC_ERROR("BE01", "A business exception has been thrown", "This action breaks a business rule", "error.business_generic_error", "error.business_generic_error_cause");
	
	private String code;
	private String value;
	private String cause;
	private String valueKey;
	private String causeKey;
	
	private BusinessExceptionEnum(final String code, final String value, final String cause, final String valueKey, final String causeKey) {
		this.code = code;
		this.value = value;
		this.cause = cause;
		this.valueKey = valueKey;
		this.causeKey = causeKey;
	}
	
	
	public static BusinessExceptionEnum valueOfCodigo(String codigo) {
		for (BusinessExceptionEnum error: BusinessExceptionEnum.values()) {
			if (error.getCode().equalsIgnoreCase(codigo)) {
				return error;
			}
		}
		return null;
	}
	
	public static BusinessExceptionEnum valueOfValueKey(String valueKey) {
		for (BusinessExceptionEnum error: BusinessExceptionEnum.values()) {
			if (error.getValueKey().equals(valueKey)) {
				return error;
			}
		}
		return null;
	}
	
	public static BusinessExceptionEnum valueOfCauseKey(String causeKey) {
		for (BusinessExceptionEnum error: BusinessExceptionEnum.values()) {
			if (error.getCauseKey().equals(causeKey)) {
				return error;
			}
		}
		return null;
	}

}
