package br.com.harvest.onboardexperience.domain.interfaces;

import org.mapstruct.InheritInverseConfiguration;


public interface CustomMapperInterface<E, D> {
	
	D toDto(E entity);

	@InheritInverseConfiguration(name = "toDto")
	E toEntity(D dto);
	
}
