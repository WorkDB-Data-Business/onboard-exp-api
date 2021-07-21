package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	
	@Query(value = "SELECT * FROM tbuser, tbclient WHERE tbuser.idclient = tbclient.idclient AND tbclient.tenant = ?2", 
			nativeQuery = true)
	List<User> findAllByTenant(String tenant);
	
	@Query(value = "SELECT * FROM tbuser, tbclient WHERE tbuser.idclient = tbclient.idclient AND tbuser.username = ?1"
			+ " AND tbclient.tenant = ?2", 
			nativeQuery = true)
	Optional<User> findByUsernameAndTenant(String username, String tenant);
	
	Optional<User> findByUsernameContainingIgnoreCaseAndClient(String username, Client client);
	
	Optional<User> findByEmailContainingIgnoreCase(String email);
	
	Optional<User> findByCpf(String cpf);
		
}
