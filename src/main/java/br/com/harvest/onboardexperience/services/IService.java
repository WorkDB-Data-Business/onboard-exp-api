package br.com.harvest.onboardexperience.services;

import java.util.List;

public interface IService <D> {

	D create(D dto);
	D update(Long id, D dto);
	D findById(Long id);
	List<D> findAll();
	void delete(Long id);
	
}
