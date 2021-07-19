package br.com.harvest.onboardexperience.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByUsernameContainingIgnoreCase(String username);
	
	Optional<User> findByEmailContainingIgnoreCase(String username);
	
	Optional<User> findByCpf(String cpf);
	
}
