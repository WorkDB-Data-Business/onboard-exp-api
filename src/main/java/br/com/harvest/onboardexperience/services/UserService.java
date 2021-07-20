package br.com.harvest.onboardexperience.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.harvest.onboardexperience.configurations.application.PasswordConfiguration;
import br.com.harvest.onboardexperience.domain.dto.ClientDto;
import br.com.harvest.onboardexperience.domain.dto.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.dto.RoleDto;
import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.InvalidCpfException;
import br.com.harvest.onboardexperience.domain.exceptions.UserAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private RoleService roleService;

	@Override
	public UserDto create(@NonNull UserDto dto) {
		try {
			validateUser(dto);

			User user = repository.save(mapper.toEntity(dto));
			
			log.info("The user " + dto.getUsername() + " was saved successful.");
			return mapper.toDto(user);
		} catch (Exception e) {
			log.error("An error has occurred when saving user " + dto.getUsername() , e);
			return null;
		}
	}

	@Override
	public UserDto update(@NonNull Long id, @NonNull UserDto dto) {
		try {
			User user = repository.findById(id).orElseThrow(
					() -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
			
			//TODO: workaround (A.K.A gambiarra) to solve the bug of password encrypt when not updated
			String[] fieldParameters = new String[2];
			fieldParameters[0] = "id";
			
			validateUser(user, dto, fieldParameters);
			
			// TODO: need to solve a bug that doesn't update the roles
			BeanUtils.copyProperties(dto, user, fieldParameters);
			
			user = repository.save(user);
			
			log.info("The user " + dto.getUsername() + " was updated successful.");

			return mapper.toDto(user);
		} catch (Exception e) {
			log.error("An error has occurred when updating user with ID " + id, e);
			return null;
		}
	}
	
	@Transactional
	public void disableUser(@NonNull final Long id) {
		try {
			User user = repository.findById(id).orElseThrow(
					() -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
			user.setIsActive(!user.getIsActive());
			repository.save(user);
			
			String isEnabled = user.getIsActive().equals(true) ? "disabled" : "enabled";
			log.info("The user with ID " + id + " was " + isEnabled + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred when disabling or enabling user with ID " + id, e);
		}
	}
	
	@Transactional
	public void expireUser(@NonNull final Long id) {
		try {
			User user = repository.findById(id).orElseThrow(
					() -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
			user.setIsExpired(!user.getIsExpired());
			repository.save(user);

			String isExpired = user.getIsExpired().equals(true) ? "expired" : "expired reverted";
			log.info("The user with ID " + id + " was " + isExpired + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred when expiring or reverting user with ID " + id, e);
		}
	}
	
	@Transactional
	public void blockUser(@NonNull final Long id) {
		try {
			User user = repository.findById(id).orElseThrow(
					() -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
			user.setIsBlocked(!user.getIsBlocked());
			repository.save(user);
			String isBlocked = user.getIsBlocked().equals(true) ? "blocked" : "desblocked";
			log.info("The user with ID " + id + " was " + isBlocked + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred when blocking or desblocking user with ID " + id, e);
		}
	}
	

	@Override
	public UserDto findById(@NonNull final Long id) {
		User user = repository.findById(id).orElseThrow(
				() -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));

		return mapper.toDto(user);
	}

	@Override
	public Page<UserDto> findAll(final Pageable pageable) {
		List<UserDto> users = repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
		return new PageImpl<>(users, pageable, users.size());
	}

	@Override
	public void delete(@NonNull final Long id) {
		try {
			User user = repository.findById(id).orElseThrow(
					() -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
			roleService.deleteRelationshipFromUser(id);
			repository.delete(user);
			log.info("The user with ID " + id + " was deleted successful.");
		} catch (Exception e) {
			log.error("An error has occurred when deleting user with ID " + id, e);
		}
	}

	private void encryptPassword(@NonNull UserDto user) {
		user.setPassword(passwordConfiguration.encoder().encode(user.getPassword()));
	}
	
	private void encryptPassword(@NonNull User user, @NonNull UserDto userDto, String[] fieldParameters) {
		if(checkIfPasswordChanged(user, userDto)) {
			encryptPassword(userDto);
		} else {
			fieldParameters[1] = "password";
		}
	}

	private void checkIfUserAlreadyExists(@NonNull UserDto dto) {
		if(repository.findByUsernameContainingIgnoreCaseAndClient(dto.getUsername(), dto.getClient()).isPresent()) {
			throw new UserAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("user", "username", dto.getUsername()));
		}

		if(repository.findByEmailContainingIgnoreCase(dto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("user", "email", dto.getEmail()));
		}
		
		if(ObjectUtils.isNotEmpty(dto.getCpf()) && repository.findByCpf(dto.getCpf()).isPresent()) {
			throw new UserAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("user", "CPF", dto.getCpf()));
		}
	}
	
	private void checkIfUserAlreadyExists(@NonNull final User user, @NonNull final UserDto dto) {
		if(!checkIfIsSameUser(user, dto)) {
			checkIfUserAlreadyExists(dto);
		}
	}

	private void validateCpf(@NonNull final UserDto dto) {

		if(ObjectUtils.isNotEmpty(dto.getCpf()) && !GenericUtils.validateCPF(dto.getCpf())) {
			throw new InvalidCpfException(dto.getCpf());
		}

	}
	
	private void validateCpf(@NonNull final User user, @NonNull final UserDto dto) {
		if(checkIfCpfChanged(user, dto)) {
			validateCpf(dto);
		}
	}	
	
	private Boolean checkIfCpfChanged(@NonNull User user, @NonNull UserDto dto) {
		Boolean hasCpf = Objects.nonNull(user.getCpf()) && Objects.nonNull(dto.getCpf());
		if(hasCpf && user.getCpf().equals(dto.getCpf())) {
			return false;
		}
		return true;
	}

	private Boolean checkIfPasswordChanged(@NonNull final User user, @NonNull final UserDto dto) {

		if(passwordConfiguration.encoder().matches(dto.getPassword(), user.getPassword())) {
			return false;
		}

		return true;
	}

	private void fetchAndSetCompanyRole(@NonNull UserDto user) {

		CompanyRoleDto companyRole = companyRoleService.findByIdOrName(user.getCompanyRole().getId(), user.getCompanyRole().getName()); 

		user.setCompanyRole(companyRole);
	}
	
	private void fetchAndSetCompanyRole(@NonNull User user, @NonNull UserDto userDto) {
		if(checkIfCompanyRoleChanged(user, userDto)) {
			fetchAndSetCompanyRole(userDto);
		}
	}


	private Boolean checkIfCompanyRoleChanged(@NonNull User user, @NonNull UserDto dto) {
		if(!user.getCompanyRole().getId().equals(dto.getCompanyRole().getId())) {
			return false;
		}
		return true;
	}
	
	private Boolean checkIfRolesChanged(@NonNull User user, @NonNull UserDto userDto) {
		return !user.getRoles().equals(userDto.getRoles());
	}
	
	private void fetchAndSetRoles(@NonNull UserDto user) {
		Set<RoleDto> roles = new HashSet<>();
		for(RoleDto roleDto : user.getRoles()) {
			roles.add(roleService.findRoleByRole(roleDto.getRole()));
		}
		user.setRoles(roles);
	}
	
	private void fetchAndSetRoles(@NonNull User user, @NonNull UserDto userDto) {
		if(checkIfRolesChanged(user, userDto)) {
			fetchAndSetRoles(userDto);
		}
	}
	
	private void fetchAndSetClient(@NonNull UserDto dto) {
		ClientDto client = clientService.findById(dto.getClient().getId());
		dto.setClient(client);
	}
	
	private void fetchAndSetClient(@NonNull User user, @NonNull UserDto dto) {
		if(checkIfClientChanged(user, dto)) {
			fetchAndSetClient(dto);
		}
	}
	
	private Boolean checkIfClientChanged(@NonNull final User user, @NonNull final UserDto dto) {
		if(user.getClient().getId().equals(dto.getClient().getId())) {
			return false;
		}
		return true;
	}
	
	private Boolean checkIfIsSameUser(@NonNull User user, @NonNull UserDto dto) {
		if(!user.getUsername().equals(dto.getUsername())) {
			return false;
		}
		
		return true;
	}
	
	private void validateUser(@NonNull UserDto dto) {
		
		checkIfUserAlreadyExists(dto);
		
		validateCpf(dto);
		
		encryptPassword(dto);
		
//		fetchAndSetClient(dto);

		fetchAndSetCompanyRole(dto);
		
		fetchAndSetRoles(dto);
		
	}
	
	private void validateUser(@NonNull User user, @NonNull UserDto dto, String[] fieldParameters) {
		
		checkIfUserAlreadyExists(user, dto);

		validateCpf(user, dto);

		encryptPassword(user, dto, fieldParameters);
		
		fetchAndSetClient(user, dto);
		
		fetchAndSetCompanyRole(user, dto);
		
		fetchAndSetRoles(user, dto);		
	}	

}
