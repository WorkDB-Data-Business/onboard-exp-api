package br.com.harvest.onboardexperience.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IService <D> {

	D create(D dto, String token);
	D update(Long id, D dto, String token);
	D findByIdAndTenant(Long id, String token);
	Page<D> findAllByTenant(Pageable pageable, String token);
	void delete(Long id, String token);
	
}
