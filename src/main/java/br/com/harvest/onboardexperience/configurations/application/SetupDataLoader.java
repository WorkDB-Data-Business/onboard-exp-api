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

import br.com.harvest.onboardexperience.domain.entities.Permission;
import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.exceptions.PermissionNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.RoleNotFoundException;
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

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup) return;

		setupPermissions();

		setupRoles();

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
				
				log.info("The load of " + permission.getPermission() + " occurred successful");
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

		if(userRepository.findByUsername("harvest").isPresent()) return;

		try {
			Role masterRole = roleRepository.findByRole(RoleEnum.MASTER).orElseThrow(() -> new RoleNotFoundException("Permission with name " + RoleEnum.MASTER.getName() + " was not found"));

			User user = User.builder()
					.firstName("User")
					.lastName("Harvest")
					.email("harvest@gmail.com")
					.username("harvest")
					.password(passwordConfiguration.encoder().encode("harvest"))
					.isActive(true)
					.isExpired(false)
					.isBlocked(false)
					.roles(Set.of(masterRole))
					.build();

			userRepository.save(user);

			log.info("The load of master user occurred successful");
		} catch (Exception e) {
			log.error("Occurred an error to load master user: " + e.getMessage(), e.getCause());
		}
	}

}