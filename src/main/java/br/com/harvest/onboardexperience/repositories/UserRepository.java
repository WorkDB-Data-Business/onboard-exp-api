package br.com.harvest.onboardexperience.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.harvest.onboardexperience.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
