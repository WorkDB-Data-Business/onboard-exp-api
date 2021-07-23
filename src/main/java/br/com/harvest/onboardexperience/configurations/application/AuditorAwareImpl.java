package br.com.harvest.onboardexperience.configurations.application;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String>{
	
    @Override
    public Optional<String> getCurrentAuditor() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String currentUsername = Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal()) ? "System" : authentication.getName(); 
    	
        return Optional.of(currentUsername);
    }

}
