package br.com.harvest.onboardexperience.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	
	//TODO: Implement the protection of endpoints using roles
	
	@Autowired
	private UserService service;
	

	@Operation(description = "Retorna os usuários cadastrados.")
	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<UserDto>> findAll(Pageable pageable) {
		return ResponseEntity.ok(service.findAll(pageable));
	}	
	
	@Operation(description = "Retorna o usuário cadastrado pelo ID.")
	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(description = "Salva um usuário no banco de dados e o retorna.")
	@PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> create(@Valid @RequestBody @NotNull UserDto dto) {
		return ResponseEntity.ok().body(service.create(dto));
	}
	
	@Operation(description = "Realiza a alteração de um usuário no banco de dados e o retorna atualizado.")
	@PutMapping(path = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @Valid @NotNull UserDto dto) {
		return ResponseEntity.ok().body(service.update(id, dto));
	}
	
	@Operation(description = "Realiza a exclusão de um usuário no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/users/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.delete(id);
	}
	
	@Operation(description = "Realiza a inativação de um usuário no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/users/disable/{id}")
	public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.disableUser(id);
	}
	
	@Operation(description = "Realiza a expiração de um usuário no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/users/expire/{id}")
	public void expire(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.expireUser(id);
	}
	
	@Operation(description = "Realiza a  de um usuário no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/users/block/{id}")
	public void block(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.blockUser(id);
	}
}
