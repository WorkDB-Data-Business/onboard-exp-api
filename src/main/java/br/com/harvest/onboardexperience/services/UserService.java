package br.com.harvest.onboardexperience.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.UserExceptionEnum;
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
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException(UserExceptionEnum.USER_NOT_FOUND_ID));
		
		BeanUtils.copyProperties(dto, user);
		
		user = repository.save(user);
		
		return mapper.toDto(user);
	}

	@Override
	public UserDto findById(Long id) {
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException(UserExceptionEnum.USER_NOT_FOUND_ID));
		
		return mapper.toDto(user);
	}

	@Override
	public List<UserDto> findAll() {
		List<User> users = repository.findAll();
		return users.stream().map(mapper::toDto).collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException(UserExceptionEnum.USER_NOT_FOUND_ID));
		repository.delete(user);
	}
		
}
