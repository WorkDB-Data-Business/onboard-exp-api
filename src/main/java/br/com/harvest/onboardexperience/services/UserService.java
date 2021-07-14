package br.com.harvest.onboardexperience.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.configurations.application.PasswordConfiguration;
import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.UserAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.UserRepository;

@Service
public class UserService implements IService<UserDto>{

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private PasswordConfiguration passwordConfiguration;
	
	@Override
	public UserDto create(UserDto dto) {
		
		encryptPassword(dto);
		
		checkIfUserAlreadyExists(dto);
		
		User user = repository.save(mapper.toEntity(dto));
		return mapper.toDto(user);
	}

	@Override
	public UserDto update(Long id, UserDto dto) {
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException("The user with ID " + id + " has not found"));
		
		checkIfUserAlreadyExists(dto);
		
		BeanUtils.copyProperties(dto, user, "id");
		
		user = repository.save(user);
		
		return mapper.toDto(user);
	}

	@Override
	public UserDto findById(Long id) {
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException("The user with ID " + id + " has not found"));
		
		return mapper.toDto(user);
	}

	@Override
	public Page<UserDto> findAll(Pageable pageable) {
		List<UserDto> users = repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
		return new PageImpl<>(users, pageable, users.size());
	}

	@Override
	public void delete(Long id) {
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException("The user with ID " + id + " has not found"));
		
		repository.delete(user);
	}
	
	private void encryptPassword(UserDto user) {
		user.setPassword(passwordConfiguration.encoder().encode(user.getPassword()));
	}
	
	private void checkIfUserAlreadyExists(UserDto dto) {
		if(repository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(dto.getUsername(),
				dto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("Try use another username or email.");
		}
	}
		
}
