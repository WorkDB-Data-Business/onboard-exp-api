package br.com.harvest.onboardexperience.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.repositories.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = repository.findByEmailContainingIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		List<GrantedAuthority> userAuthorities = getAuthorities(user.getRoles());
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getIsActive(), !user.getIsExpired(), !user.getIsExpired(),
				!user.getIsBlocked(), userAuthorities);
	}
	
	private List<GrantedAuthority> getAuthorities(Set<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role: roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
			role.getPermissions().stream()
			.map(p -> new SimpleGrantedAuthority(p.getPermission().getName()))
			.forEach(authorities::add);
		}

		return authorities;
	}

}
