package br.com.harvest.onboardexperience.domain.enumerators;

import lombok.Getter;

@Getter
public enum FileTypeEnum {

	COIN("coin"),
	REWARD("reward"),
	IMAGE("image");

	private String name;

	FileTypeEnum(String name) {
		this.name = name;
	}

}
