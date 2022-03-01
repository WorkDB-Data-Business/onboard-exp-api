package br.com.harvest.onboardexperience.infra.auth.services;

import java.util.*;

import br.com.harvest.onboardexperience.domain.entities.Permission;
import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
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
        Set<GrantedAuthority> authorities = new HashSet<>();

        roles.stream().map(Role::getRole).map(RoleEnum::getName)
                .map(String::toUpperCase)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        roles.stream().map(Role::getPermissions)
                .flatMap(Collection::stream)
                .map(Permission::getPermission)
                .map(PermissionEnum::getName)
                .map(String::toUpperCase)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        return new ArrayList<>(authorities);
    }

}
