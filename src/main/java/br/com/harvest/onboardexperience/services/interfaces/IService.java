package br.com.harvest.onboardexperience.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.NonNull;

public interface IService <D> {

	D create(@NonNull D dto, @NonNull final String token);
	D update(@NonNull final Long id, @NonNull D dto, @NonNull final String token);
	D findByIdAndTenant(@NonNull final Long id, @NonNull final String token);
	Page<D> findAllByTenant(Pageable pageable, @NonNull final String token);
	void delete(@NonNull final Long id, @NonNull final String token);
	
}
