package br.com.harvest.onboardexperience.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.configuration.environment.EnvironmentVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Home")
@RestController
@RequestMapping("/")
public class Controller {
	
	@Autowired
	private EnvironmentVariable environment;
	
	@Operation(description = "Faz alguma coisa")
	@GetMapping
	public String helloWorld() {
		return environment.getDataSourceUrl();
	}
	
}
