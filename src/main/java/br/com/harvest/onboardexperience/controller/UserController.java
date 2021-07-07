package br.com.harvest.onboardexperience.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
@RestController("/users")
public class UserController {
	
	
	@Operation(description = "Faz alguma coisa")
	@GetMapping
	public String helloWorld() {
		return "";
	}

}
