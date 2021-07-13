package br.com.harvest.onboardexperience.controllers;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
@RestController
@RequestMapping("/v1")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Operation(description = "Retorna os usuários cadastrados.")
	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDto>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(description = "Retorna o usuário cadastrado pelo ID.")
	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(description = "Salva um usuário no banco de dados e o retorna.")
	@PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> create(@RequestBody @NotNull UserDto dto) {
		return ResponseEntity.ok().body(service.create(dto));
	}
	

}
