package br.com.harvest.onboardexperience.utils;

import lombok.Getter;

@Getter
public class RegexUtils {

	public static final	String ONLY_NUMBERS = "^[0-9]*$";
	
	// Validate the most use email use cases
	public static final String EMAIL_VALIDATION = "[a-z0-9!#$%&'*+\\=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	// at least: 8 characters, 1 numeric, 1 lowercase letter, 1 uppercase letter and 1 special character
	public static final String PASSWORD_VALIDATION = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{8,}$";
	
}
