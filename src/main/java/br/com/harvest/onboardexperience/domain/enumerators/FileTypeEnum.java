package br.com.harvest.onboardexperience.domain.enumerators;

import lombok.Getter;

@Getter
public enum FileTypeEnum {

	COIN("coin"),
	REWARD("reward"),
	ASSET("asset");

	private String name;

	FileTypeEnum(String name) {
		this.name = name;
	}

}
