package br.com.harvest.onboardexperience.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;

public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Optional<Role> findByRole(RoleEnum role);

}
