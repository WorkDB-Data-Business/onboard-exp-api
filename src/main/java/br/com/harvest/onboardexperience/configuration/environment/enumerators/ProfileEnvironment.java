package br.com.harvest.onboardexperience.configuration.environment.enumerators;

import lombok.Getter;

@Getter
public enum ProfileEnvironment {
	
	DEV("Development"), PROD("Production"), TEST("Test");
	
	private String name;

	private ProfileEnvironment(String name) {
		this.name = name;
	}

}
