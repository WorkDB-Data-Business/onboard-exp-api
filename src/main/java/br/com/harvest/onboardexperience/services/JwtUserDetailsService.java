package br.com.harvest.onboardexperience.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.repositories.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		
		//TODO: Implement the logic of roles
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getIsActive(), !user.getIsExpired(), !user.getIsExpired(),
				!user.getIsBlocked(), new ArrayList<>());
	}
	
}
