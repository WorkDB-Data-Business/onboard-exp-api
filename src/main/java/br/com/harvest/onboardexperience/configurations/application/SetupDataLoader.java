package br.com.harvest.onboardexperience.configurations.application;


import br.com.harvest.onboardexperience.services.*;
import br.com.harvest.onboardexperience.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.TimeZone;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private CompanyRoleService companyRoleService;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup) return;

		permissionService.populatePermissions();

		roleService.populateRoles();
		
		clientService.saveHarvestClient();
		
		companyRoleService.saveHarvestCompanyRole();

		userService.saveHarvestUser();

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Locale.setDefault(Constants.Date.LOCALE_BRASIL);

		alreadySetup = true;
	}

}