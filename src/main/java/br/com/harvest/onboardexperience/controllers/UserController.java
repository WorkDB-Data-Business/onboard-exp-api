package br.com.harvest.onboardexperience.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.harvest.onboardexperience.domain.dto.UserForm;
import br.com.harvest.onboardexperience.infra.email.EmailMessage;
import br.com.harvest.onboardexperience.infra.email.EmailSender;
import br.com.harvest.onboardexperience.infra.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;
import java.util.Set;

@Tag(name = "Users")
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 36000)
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private EmailSender emailSender;

	@Operation(description = "Retorna os usuários cadastrados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<UserDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(pageable, token));
	}	
	
	@Operation(description = "Retorna o usuário cadastrado pelo ID.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByIdAndTenant(id, token));
	}
	
	@Operation(description = "Retorna o usuário autenticado.")
	@GetMapping(path = "/users/my-user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> findMyUser(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findMyUser(token));
	}
	
	@Operation(description = "Salva um usuário no banco de dados e o retorna.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> create(@Valid @RequestBody @NotNull UserForm dto, @RequestHeader("Authorization") String token) throws RuntimeException {
		return ResponseEntity.ok().body(service.create(dto, token));
	}
	
	@Operation(description = "Realiza a alteração de um usuário no banco de dados e o retorna atualizado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @Valid @NotNull UserForm dto, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.update(id, dto, token));
	}
	
	@Operation(description = "Realiza a exclusão de um usuário no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/users/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.delete(id, token);
	}
	
	@Operation(description = "Realiza a inativação de um usuário no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = "/users/disable/{id}")
	public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.disableUser(id, token);
	}
	
	@Operation(description = "Realiza a expiração de um usuário no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/users/expire/{id}")
	public void expire(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.expireUser(id, token);
	}
	
	@Operation(description = "Realiza a de um usuário no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/users/block/{id}")
	public void block(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.blockUser(id, token);
	}

}
