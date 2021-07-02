package br.com.harvest.onboardexperience.configuration.environment.enumerators;

import lombok.Getter;

@Getter
public enum ProfileEnvironment {
	
	DEV("dev"), PROD("prod");
	
	private String name;

	private ProfileEnvironment(String name) {
		this.name = name;
	}

}
