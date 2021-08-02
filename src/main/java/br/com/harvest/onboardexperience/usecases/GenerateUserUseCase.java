package br.com.harvest.onboardexperience.usecases;

import java.util.Set;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.harvest.onboardexperience.configurations.application.PasswordConfiguration;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.repositories.CompanyRoleRepository;
import br.com.harvest.onboardexperience.repositories.RoleRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GenerateUserUseCase {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CompanyRoleRepository companyRoleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordConfiguration passwordConfiguration;
	
	public void createAdminUserFromClient(@NonNull final Client client) {
		try {
			Role role = roleRepository.findByRole(RoleEnum.ADMIN).orElseThrow(() -> new RoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("role", "name", RoleEnum.ADMIN.getName())));
			CompanyRole companyRole = createAdminCompanyRoleFromClient(client);
			User user = User.builder().client(client)
					.companyRole(companyRole)
					.email(client.getEmail())
					.isClient(true)
					.username(GenericUtils.formatNameToUsername(client.getName()))
					.firstName(client.getName())
					.isActive(true)
					.isFirstLogin(true)
					.isBlocked(false)
					.isExpired(false)
					.lastName("User")
					.isFirstLogin(true)
					.password(passwordConfiguration.encoder().encode(client.getName()))
					.roles(Set.of(role))
					.build();
			userRepository.save(user);
			log.info("Admin user from client of ID " + client.getId() + " created successful.");
		} catch (Exception e) {
			log.error("An error has occurred while creating user admin from client of ID " + client.getId(), e);
		}
	}
	
	private CompanyRole createAdminCompanyRoleFromClient(@NonNull final Client client) {
		try {
			CompanyRole companyRole = companyRoleRepository.save(CompanyRole.builder()
					.client(client).name(client.getName() + " Admin User").build());  
			log.info("Admin company role from client of ID " + client.getId() + " created successful.");
			return companyRole;
		} catch (Exception e) {
			log.error("An error has occurred while creating company role admin from client of ID " + client.getId(), e);
			return null;
		}
	}

}
