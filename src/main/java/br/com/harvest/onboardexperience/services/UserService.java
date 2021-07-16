package br.com.harvest.onboardexperience.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.configurations.application.PasswordConfiguration;
import br.com.harvest.onboardexperience.domain.dto.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.InvalidCpfException;
import br.com.harvest.onboardexperience.domain.exceptions.UserAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;

@Service
public class UserService implements IService<UserDto>{

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserMapper mapper;

	@Autowired
	private PasswordConfiguration passwordConfiguration;

	@Autowired
	private CompanyRoleService companyRoleService;

	@Override
	public UserDto create(UserDto dto) {

		encryptPassword(dto);

		checkIfUserAlreadyExists(dto);

		validateCpf(dto);

		fetchAndSetCompanyRole(dto);

		User user = repository.save(mapper.toEntity(dto));
		return mapper.toDto(user);
	}

	@Override
	public UserDto update(Long id, UserDto dto) {
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException("The user with ID " + id + " has not found"));

		checkIfUserAlreadyExists(dto);

		validateCpf(dto);

		fetchAndSetCompanyRole(user, dto);

		encryptPassword(user, dto);

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
	
	private void encryptPassword(User user, UserDto userDto) {
		if(checkIfPasswordChanged(user, userDto)) {
			encryptPassword(userDto);
		}
	}

	private void checkIfUserAlreadyExists(UserDto dto) {
		if(repository.findByUsernameContainingIgnoreCase(dto.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException("Try use another username.");
		}

		if(repository.findByEmailContainingIgnoreCase(dto.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException("Try use another email.");
		}
	}

	private void validateCpf(UserDto dto) {

		if(ObjectUtils.isNotEmpty(dto.getCpf()) && !GenericUtils.validateCPF(dto.getCpf())) {
			throw new InvalidCpfException("CPF is invalid");
		}

	}

	private Boolean checkIfPasswordChanged(User user, UserDto dto) {

		if(passwordConfiguration.encoder().matches(dto.getPassword(), user.getPassword())) {
			return false;
		}

		return true;
	}

	private void fetchAndSetCompanyRole(UserDto user) {

		CompanyRoleDto companyRole = companyRoleService.findByIdOrName(user.getCompanyRole().getId(), user.getCompanyRole().getName()); 

		user.setCompanyRole(companyRole);
	}
	
	private void fetchAndSetCompanyRole(User user, UserDto userDto) {
		if(checkIfCompanyRoleChanged(user, userDto)) {
			fetchAndSetCompanyRole(userDto);
		}
	}


	private Boolean checkIfCompanyRoleChanged(User user, UserDto dto) {
		if(!user.getCompanyRole().getId().equals(dto.getCompanyRole().getId())) {
			return false;
		}
		return true;
	}

}
