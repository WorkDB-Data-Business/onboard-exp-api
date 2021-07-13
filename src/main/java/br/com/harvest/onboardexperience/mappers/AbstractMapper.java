package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.InheritInverseConfiguration;

public interface AbstractMapper<E, D> {

	E toEntity(D dto);

	@InheritInverseConfiguration(name = "toEntity")
	D toDto(E entity);

}
