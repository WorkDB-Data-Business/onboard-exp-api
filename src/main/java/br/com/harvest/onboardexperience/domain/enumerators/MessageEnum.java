package br.com.harvest.onboardexperience.domain.enumerators;

import br.com.harvest.onboardexperience.domain.enumerators.interfaces.EnumInterface;

public enum MessageEnum implements EnumInterface {
	
	GENERIC_MESSAGE("M01", "Generic message", "message.generic_message"),
	GENERIC_ERROR_MESSAGE("M02", "Request support for help with this issue", "message.generic_error_message");
	
	private String code;
	private String value;
	private String valueKey;
	
	MessageEnum(final String code, final String value, final String valueKey){
		this.code = code;
		this.value = value;
		this.valueKey = valueKey;
	}
	

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getValueKey() {
		return this.valueKey;
	}

}
