package br.com.harvest.onboardexperience.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IService <D> {

	D create(D dto);
	D update(Long id, D dto);
	D findById(Long id);
	Page<D> findAll(Pageable pageable);
	void delete(Long id);
	
}
