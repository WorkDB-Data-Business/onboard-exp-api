package br.com.harvest.onboardexperience.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.domain.exception.FactoryException;
import br.com.harvest.onboardexperience.domain.exception.enumerators.FactoryExceptionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
@RestController
@RequestMapping("/users")
public class UserController {
	
	
	@Operation(description = "Faz alguma coisa")
	@GetMapping
	public String helloWorld() {
		throw new FactoryException(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL);
	}

}
