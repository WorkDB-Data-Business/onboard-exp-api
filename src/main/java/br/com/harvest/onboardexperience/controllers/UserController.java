package br.com.harvest.onboardexperience.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.domain.exceptions.FactoryException;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.FactoryExceptionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
@RestController
@RequestMapping("/v1")
public class UserController {
	
	
	@Operation(description = "Retorna os usuários cadastrados.")
	@GetMapping(path = "/users")
	public String hello() {
		throw new FactoryException(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL);
	}

}
