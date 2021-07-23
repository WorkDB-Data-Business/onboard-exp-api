package br.com.harvest.onboardexperience.domain.enumerators;

import lombok.Getter;

@Getter
public enum PermissionEnum {

	READ("READ"), WRITE("WRITE"), DELETE("DELETE"), UPDATE("UPDATE");
	
	private String name;
	
	PermissionEnum(String name) {
		this.name = name;
	}

}
