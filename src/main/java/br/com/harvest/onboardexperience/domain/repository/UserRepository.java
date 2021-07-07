package br.com.harvest.onboardexperience.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.harvest.onboardexperience.domain.entity.User;

public interface UserRepository {
	
	User create(User user);
	
	User update(User user);
	
	Optional<User> find(Long id);
	
	Page<User> find(Pageable pageable);
	
	void delete(User user);
	
}
