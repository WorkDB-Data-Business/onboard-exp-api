package br.com.harvest.onboardexperience.domain.enumerators;

import lombok.Getter;

@Getter
public enum RoleEnum {
	
	MASTER("MASTER"), ADMIN("ADMIN"), COLABORATOR("COLABORATOR");
	
	private String name;
	
	RoleEnum(String name){
		this.name = name;
	}

}
