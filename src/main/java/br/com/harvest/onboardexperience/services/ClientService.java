package br.com.harvest.onboardexperience.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.dto.ClientDto;
import br.com.harvest.onboardexperience.repositories.ClientRepository;

@Service
public class ClientService implements IService<ClientDto>{

	@Autowired
	private ClientRepository repository;
	
	@Override
	public ClientDto create(ClientDto dto) {
		
		return null;
	}

	@Override
	public ClientDto update(Long id, ClientDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDto findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ClientDto> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
