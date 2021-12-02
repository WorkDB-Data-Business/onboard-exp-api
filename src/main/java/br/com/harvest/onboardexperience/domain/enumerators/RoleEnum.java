package br.com.harvest.onboardexperience.domain.enumerators;

import lombok.Getter;

import java.util.*;

@Getter
public enum RoleEnum {
	
	MASTER("Master",
			Set.of(PermissionEnum.DELETE, PermissionEnum.READ, PermissionEnum.UPDATE, PermissionEnum.WRITE)),
	ADMIN("Admin",
			Set.of(PermissionEnum.DELETE, PermissionEnum.READ, PermissionEnum.UPDATE, PermissionEnum.WRITE)),
	EMPLOYEE("Employee",
			Set.of(PermissionEnum.READ, PermissionEnum.UPDATE));

	private String name;

	private Set<PermissionEnum> permissions;

	RoleEnum(String name, Set<PermissionEnum> permissions) {
		this.name = name;
		this.permissions = permissions;
	}

	public static List<RoleEnum> getRolesAsList(){
		return Arrays.asList(RoleEnum.values());
	}

}
