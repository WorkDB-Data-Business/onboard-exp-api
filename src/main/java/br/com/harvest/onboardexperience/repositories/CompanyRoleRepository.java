package br.com.harvest.onboardexperience.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.CompanyRole;

@Repository
public interface CompanyRoleRepository extends JpaRepository<CompanyRole, Long>{
	
	Optional<CompanyRole> findByNameContainingIgnoreCase(String name);
	
	Optional<CompanyRole> findByIdOrNameContainingIgnoreCase(Long id, String name);

}
