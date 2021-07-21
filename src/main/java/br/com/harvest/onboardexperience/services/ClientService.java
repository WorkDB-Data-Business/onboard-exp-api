package br.com.harvest.onboardexperience.services;

import java.util.List;
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

import br.com.harvest.onboardexperience.domain.dto.ClientDto;
import br.com.harvest.onboardexperience.domain.dto.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.exceptions.ClientAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.ClientNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.InvalidCnpjException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.mappers.CompanyRoleMapper;
import br.com.harvest.onboardexperience.mappers.RoleMapper;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.ClientRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Autowired
	private ClientMapper mapper;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyRoleService companyRoleService;

	
	@Transactional
	public ClientDto create(@NonNull ClientDto dto) {
		try {
			validateClient(dto);

			Client client = repository.save(mapper.toEntity(dto));
			log.info("The client with CNPJ " + dto.getCnpj() + " was saved successful.");
			createAdminUserFromClient(client);
			return mapper.toDto(client);
		} catch(Exception e) {
			log.error("An error has occurred while saving client with CNPJ " + dto.getCnpj(), e);
			return null;
		}
	}

	
	@Transactional
	public ClientDto update(@NonNull final Long id, @NonNull ClientDto dto) {
		try {
			Client client = repository.findById(id).orElseThrow(
					() -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));

			validateClient(client, dto);

			BeanUtils.copyProperties(dto, client, "id");

			client = repository.save(client);
			
			log.info("The client with CNPJ " + dto.getCnpj() + " was updated successful.");

			return mapper.toDto(client);
		} catch(Exception e) {
			log.error("An error has occurred while updating client with CNPJ " + dto.getCnpj(), e);
			return null;
		}
	}

	
	public ClientDto findById(@NonNull final Long id) {
		return mapper.toDto(repository.findById(id).orElseThrow(
				() -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString()))));
	}

	
	public Page<ClientDto> findAll(Pageable pageable) {
		List<ClientDto> clients = repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
		return new PageImpl<>(clients, pageable, clients.size());
	}

	
	@Transactional
	public void delete(@NonNull final Long id) {
		try {
			Client client = repository.findById(id).orElseThrow(
					() -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));

			repository.delete(client);
		} catch(Exception e) {
			log.error("An error has occurred while deleting client with ID " + id, e);
		}
	}
	

	@Transactional
	public void disableClient(@NonNull final Long id) {
		try {
			Client client = repository.findById(id).orElseThrow(
					() -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));
			Boolean isActive = !client.getIsActive();
			client.setIsActive(isActive);
			repository.save(client);
			repository.disableAllUsersFromAClient(isActive, client.getId());
			
			String isEnabled = isActive ? "enabled" : "disabled";
			
			log.info("The client with ID " + id + " was " + isEnabled +" successful.");
			log.info("The users from the client with ID " + id + " was " + isEnabled + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred while disabling or enabling client with ID " + id, e);
		}
	}
	
	@Transactional
	public void expireClient(@NonNull final Long id) {
		try {
			Client client = repository.findById(id).orElseThrow(
					() -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));
			Boolean isExpired = !client.getIsExpired();
			client.setIsExpired(isExpired);
			repository.save(client);
			repository.expireAllUsersFromAClient(isExpired, client.getId());
			
			String expired = isExpired ? "expired" : "expired reverted";
			
			log.info("The client with ID " + id + " was " + expired +" successful.");
			log.info("The users from the client with ID " + id + " was " + expired + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred while expiring or reverting expiration from client with ID " + id, e);
		}
	}
	
	@Transactional
	public void blockClient(@NonNull final Long id) {
		try {
			Client client = repository.findById(id).orElseThrow(
					() -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));
			Boolean isBlocked = !client.getIsBlocked();
			client.setIsExpired(isBlocked);
			repository.save(client);
			repository.blockAllUsersFromAClient(isBlocked, client.getId());
			
			String blocked = isBlocked ? "blocked" : "disblocked";
			
			log.info("The client with ID " + id + " was " + blocked +" successful.");
			log.info("The users from the client with ID " + id + " was " + blocked + " successful.");
		} catch (Exception e) {
			log.error("An error has occurred while blocking or disblocking client with ID " + id, e);
		}
	}

	private void validateCnpj(@NonNull final ClientDto dto) {

		if(ObjectUtils.isNotEmpty(dto.getCnpj()) && !GenericUtils.validateCNPJ(dto.getCnpj())) {
			throw new InvalidCnpjException(dto.getCnpj());
		}

	}

	private void validateCnpj(@NonNull final Client client, @NonNull final ClientDto dto) {

		if(checkIfCnpjChanged(client, dto)) {
			validateCnpj(dto);
		}
		
	}

	private Boolean checkIfCnpjChanged(@NonNull final Client client, @NonNull final ClientDto dto) {
		if(client.getCnpj().equals(dto.getCnpj())) {
			return false;
		}

		return true;
	}

	private void validateClient(@NonNull ClientDto dto) {
		checkIfClientAlreadyExists(dto);
		validateCnpj(dto);
	}

	private void validateClient(@NonNull Client client, @NonNull ClientDto dto) {
		validateCnpj(client, dto);
	}
	
	private void checkIfClientAlreadyExists(@NonNull ClientDto dto) {
		if(repository.findByCnpj(dto.getCnpj()).isPresent()) {
			throw new ClientAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("client", "CNPJ", dto.getCnpj()));
		}
		
	}
	
	private void createAdminUserFromClient(@NonNull final Client client) {
		try {
			Role role = RoleMapper.INSTANCE.toEntity(roleService.findRoleByRole(RoleEnum.ADMIN));
			CompanyRole companyRole = createAdminCompanyRoleFromClient(client);
			User user = User.builder().client(client)
					.companyRole(companyRole)
					.email(client.getEmail())
					.isClient(true)
					.username(GenericUtils.formatNameToUsername(client.getName()))
					.firstName(client.getName())
					.isActive(true)
					.isBlocked(false)
					.isExpired(false)
					.lastName("User")
					.password(client.getName())
					.roles(Set.of(role))
					.build();
			userService.create(UserMapper.INSTANCE.toDto(user));
			log.info("Admin user from client of ID " + client.getId() + " created successful.");
		} catch (Exception e) {
			log.error("An error has occurred while creating user admin from client of ID " + client.getId(), e);
		}
	}
	
	private CompanyRole createAdminCompanyRoleFromClient(@NonNull final Client client) {
		try {
			CompanyRole companyRole = CompanyRoleMapper.INSTANCE.toEntity(companyRoleService.create(CompanyRoleDto.builder()
					.client(ClientMapper.INSTANCE.toDto(client)).name(client.getName() + " Admin User").build()));  
			log.info("Admin company role from client of ID " + client.getId() + " created successful.");
			return companyRole;
		} catch (Exception e) {
			log.error("An error has occurred while creating company role admin from client of ID " + client.getId(), e);
			return null;
		}
	}
	

}