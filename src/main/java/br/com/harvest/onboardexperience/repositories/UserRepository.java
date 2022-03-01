package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Page<User> findAllByClient_Tenant(String tenant, Pageable pageable);
	Optional<User> findByUsernameAndClient_Tenant(String username, String tenant);
	Optional<User> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<User> findByUsernameContainingIgnoreCaseAndClient(String username, Client client);
	Optional<User> findByEmailContainingIgnoreCase(String email);
	Optional<User> findByCpf(String cpf);
	List<User> findAllByClient(Client client);

	@Query(value="SELECT u FROM User u WHERE " +
			"u.client.tenant = :tenant AND (" +
			"UPPER(u.firstName) LIKE '%'|| UPPER(:criteria) ||'%' OR " +
			"UPPER(u.lastName) LIKE '%'|| UPPER(:criteria) ||'%' OR " +
			"UPPER(u.username) LIKE '%'|| UPPER(:criteria) ||'%' OR " +
			"UPPER(u.email) LIKE '%'|| UPPER(:criteria) ||'%' OR " +
			"u.cpf like '%'|| :criteria ||'%')")
	Page<User> findByCriteria(String criteria, String tenant, Pageable pageable);
}
