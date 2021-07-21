package br.com.harvest.onboardexperience.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.TenantForbiddenException;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;

@Service
public class TenantService {
	
	@Autowired
	private JwtTokenUtils jwtUtils;
	
	public void validateTenant(@NonNull final String token, @NonNull final User user) {
		String tenantToken = jwtUtils.getUsernameTenant(token); 
		String userTenant = user.getClient().getTenant();
		if(!tenantToken.equalsIgnoreCase(userTenant)) {
			throw new TenantForbiddenException("The tenant from token and from user is different.", new Throwable("Operations in another tenant is forbidden."));
		}
	}

}
