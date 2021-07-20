package br.com.harvest.onboardexperience.configurations.application;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.entities.Permission;
import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.exceptions.ClientNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.CompanyRoleNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.PermissionNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.RoleNotFoundException;
import br.com.harvest.onboardexperience.repositories.ClientRepository;
import br.com.harvest.onboardexperience.repositories.CompanyRoleRepository;
import br.com.harvest.onboardexperience.repositories.PermissionRepository;
import br.com.harvest.onboardexperience.repositories.RoleRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private PasswordConfiguration passwordConfiguration;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRoleRepository companyRoleRepository;
	
	@Autowired
	private ClientRepository clientRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup) return;

		setupPermissions();

		setupRoles();
		
		setupMasterClient();
		
		setupMasterCompanyRole();

		setupMasterUser();

		alreadySetup = true;
	}

	@Transactional
	void setupPermissions() {
		try {

			for(PermissionEnum permissionEnum : PermissionEnum.values()) {

				if(permissionRepository.findByPermission(permissionEnum).isPresent()) continue;

				Permission permission = new Permission();
				permission.setPermission(permissionEnum);
				permissionRepository.save(permission);

				log.info("The load of " + permission.getPermission() + " permission  occurred successful");
			}

		} catch (Exception e) {
			log.error("Occurred an error to load permissions: " + e.getMessage(), e.getCause());
			return;
		}
	}

	@Transactional
	private void setupRole(RoleEnum roleParam, Set<Permission> permissions) {

		if(roleRepository.findByRole(roleParam).isPresent()) return;

		if(Objects.isNull(roleParam)) {
			throw new NullPointerException("Role cannot be null on setup");
		}

		if(ObjectUtils.isEmpty(permissions)) {
			throw new NullPointerException("Permissions cannot be null or empty on setup");
		}

		try {
			Role role = Role.builder().role(roleParam).permissions(permissions).build();

			roleRepository.save(role);

			log.info("The load of role " + role.getRole() +  " occurred successful");
		} catch (Exception e) {
			log.error("Occurred an error to load roles: " + e.getMessage(), e.getCause());
		}
	}

	private void setupRoles() {

		setupRole(RoleEnum.ADMIN, createPermissionsRole(RoleEnum.ADMIN));
		setupRole(RoleEnum.MASTER, createPermissionsRole(RoleEnum.MASTER));
		setupRole(RoleEnum.COLABORATOR, createPermissionsRole(RoleEnum.COLABORATOR));

	}

	private Set<Permission> createPermissionsRole(RoleEnum role) {
		switch(role) {
			case ADMIN: {
				return createPermissions(PermissionEnum.WRITE, PermissionEnum.READ, PermissionEnum.UPDATE, PermissionEnum.DELETE);
			}
			case MASTER: {
				return createPermissions(PermissionEnum.WRITE, PermissionEnum.READ, PermissionEnum.UPDATE, PermissionEnum.DELETE);
			}
			case COLABORATOR: {
				return createPermissions(PermissionEnum.READ, PermissionEnum.UPDATE);
			}
			default: {
				return null;
			}
		}
	}

	private Set<Permission> createPermissions(PermissionEnum... permissionsEnum){
		Set<Permission> permissions = new HashSet<>();
		for(PermissionEnum permissionEnum : permissionsEnum) {
			Permission permission = permissionRepository.findByPermission(permissionEnum).orElseThrow(() -> new PermissionNotFoundException("Permission with name " + permissionEnum.getName() + " was not found"));
			permissions.add(permission);
		}

		return permissions;
	}

	@Transactional
	private void setupMasterUser() {

		if(userRepository.findByUsernameAndTenant("harvest", "harvest").isPresent()) return;

		try {
			Role masterRole = roleRepository.findByRole(RoleEnum.MASTER).orElseThrow(() -> new RoleNotFoundException("Permission with name " + RoleEnum.MASTER.getName() + " was not found"));
			CompanyRole companyRole = companyRoleRepository.findByNameContainingIgnoreCase("system owner").orElseThrow(() -> new CompanyRoleNotFoundException("Company Role with name system owner not found"));
			Client client = clientRepository.findByTenantContainingIgnoreCase("harvest").orElseThrow(() -> new ClientNotFoundException("Client with name harvest not found"));
			
			User user = User.builder()
					.firstName("User")
					.lastName("Harvest")
					.email("harvest@gmail.com")
					.username("harvest")
					.password(passwordConfiguration.encoder().encode("harvest"))
					.isActive(true)
					.isExpired(false)
					.isBlocked(false)
					.client(client)
					.isClient(true)
					.companyRole(companyRole)
					.roles(Set.of(masterRole))
					.build();

			userRepository.save(user);

			log.info("The load of master user occurred successful");
		} catch (Exception e) {
			log.error("Occurred an error to load master user: " + e.getMessage(), e.getCause());
		}
	}
	
	@Transactional
	private void setupMasterCompanyRole() {
		if(companyRoleRepository.findByNameContainingIgnoreCase("system owner").isPresent()) return; 
		
		Client client = clientRepository.findByTenantContainingIgnoreCase("harvest").orElseThrow(() -> new ClientNotFoundException("Client with name harvest not found"));
		
		try {
			CompanyRole companyRole = CompanyRole.builder().name("System Owner").client(client).build();
			companyRoleRepository.save(companyRole);
			log.info("The load of master user's company role occurred successful");
		} catch (Exception e) {
			log.error("Occurred an error to load master user's company role: " + e.getMessage(), e.getCause());
		}
	}
	
	@Transactional
	private void setupMasterClient() {
		if(clientRepository.findByNameContainingIgnoreCase("harvest").isPresent()) return;
			//TODO: fill with correct cnpj later
			Client client = Client.builder().cnpj("12345678912345")
					.name("Harvest")
					.isActive(true)
					.isBlocked(false)
					.isExpired(false)
					.isMaster(true)
					.tenant("harvest")
					.build();
			
			log.info("The load of master client occurred successful");
			clientRepository.save(client);
		try {
			
		} catch(Exception e) {
			log.error("Occurred an error to load master client: " + e.getMessage(), e.getCause());
		}
	}

}