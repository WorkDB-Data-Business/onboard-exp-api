package br.com.harvest.onboardexperience.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.UserRepository;

@Service
public class UserService implements IService<UserDto>{

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private UserMapper mapper;
	
	@Override
	public UserDto create(UserDto dto) {
		User user = repository.save(mapper.toEntity(dto));
		return mapper.toDto(user);
	}

	@Override
	public UserDto update(Long id, UserDto dto) {
		User user = repository.findById(id).orElseThrow(() -> );
		if() {
			
		}
		return null;
	}

	@Override
	public UserDto findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDto> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}
		
}
