package br.com.harvest.onboardexperience.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

	Optional<Client> findByNameContainingIgnoreCase(String name);
	
	Optional<Client> findByTenantContainingIgnoreCase(String tenant);
	
}
