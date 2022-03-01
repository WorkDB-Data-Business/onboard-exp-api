package br.com.harvest.onboardexperience.domain.enumerators;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PermissionEnum {

	READ("READ"), WRITE("WRITE"), DELETE("DELETE"), UPDATE("UPDATE");
	
	private String name;
	
	PermissionEnum(String name) {
		this.name = name;
	}

	public static List<PermissionEnum> getPermissionsAsList(){
		return Arrays.asList(PermissionEnum.values());
	}
}
