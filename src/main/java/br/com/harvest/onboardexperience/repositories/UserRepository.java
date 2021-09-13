package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findAllByClient_Tenant(String tenant);
	Optional<User> findByUsernameAndClient_Tenant(String username, String tenant);
	Optional<User> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<User> findByUsernameContainingIgnoreCaseAndClient(String username, Client client);
	Optional<User> findByEmailContainingIgnoreCase(String email);
	Optional<User> findByCpf(String cpf);
		
}
