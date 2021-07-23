package br.com.harvest.onboardexperience.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.repositories.UserRepository;

@Service
public class LoginUseCase {
	
	@Autowired
	UserRepository userRepository;
	
	@Transactional
	public void doFirstLoginByEmail(String email) {
		User user = userRepository.findByEmailContainingIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException(ExceptionMessageFactory
				.createNotFoundMessage("user", "email", email)));
		if(user.getIsFirstLogin()) {
			user.setIsFirstLogin(false);
		}
	}

}
