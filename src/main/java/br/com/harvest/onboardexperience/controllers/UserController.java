package br.com.harvest.onboardexperience.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.harvest.onboardexperience.domain.dtos.UserSimpleDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.ChangePasswordForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.UserForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.UserWelcomeForm;
import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationDto;
import br.com.harvest.onboardexperience.infra.notification.services.NotificationService;
import br.com.harvest.onboardexperience.usecases.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Users")
@RestController
@RequestMapping("/v1/users")
@CrossOrigin(origins = "*", maxAge = 36000)
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private UserUseCase useCase;

	@Autowired
	private NotificationService notificationService;

	@Operation(description = "Retorna os usuários cadastrados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<UserDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(pageable, token));
	}

	@Operation(description = "Retorna os usuários cadastrados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserSimpleDto>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(token));
	}

	@Operation(description = "Retorna os usuários com base no valor buscado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/find/{criteria}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<UserDto>> findbyCriteria(@PathVariable String criteria, Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByCriteria(criteria, pageable, token));
	}

	@Operation(description = "Retorna o usuário cadastrado pelo ID.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> findById(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findUserDtoByIdAndTenant(id, token));
	}
	
	@Operation(description = "Retorna o usuário autenticado.")
	@GetMapping(path = "/my-user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> findMyUser(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findMyUser(token));
	}
	
	@Operation(description = "Salva um usuário no banco de dados e o retorna.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> create(@Valid @RequestBody @NotNull UserForm dto, @RequestHeader("Authorization") String token) throws RuntimeException {
		return ResponseEntity.ok().body(service.create(dto, token));
	}
	
	@Operation(description = "Realiza a alteração de um usuário no banco de dados e o retorna atualizado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @Valid @NotNull UserForm dto, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.update(id, dto, token));
	}
	
	@Operation(description = "Realiza a exclusão de um usuário no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.delete(id, token);
	}
	
	@Operation(description = "Realiza a inativação de um usuário no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = "/disable/{id}")
	public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.disableUser(id, token);
	}
	
	@Operation(description = "Realiza a expiração de um usuário no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/expire/{id}")
	public void expire(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.expireUser(id, token);
	}
	
	@Operation(description = "Realiza o bloqueio de um usuário no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/block/{id}")
	public void block(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.blockUser(id, token);
	}

	@Operation(description = "Realiza a inserção do formulário de boas-vindas de um usuário no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR') or hasAuthority('MASTER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/welcome/{id}")
	public void welcome(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @NotNull @Valid UserWelcomeForm form, @RequestHeader("Authorization") String token) {
		useCase.welcomeUser(id, form, token);
	}

	@Operation(description = "Realiza a alteração da senha no primeiro acesso.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/welcome/change-password/{id}")
	public void changePasswordFirstAccess(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @NotNull @Valid ChangePasswordForm form, @RequestHeader("Authorization") String token) {
		useCase.changePassword(id, form, token);
	}

	@Operation(description = "Realiza a busca das notificações do usuário.")
	@GetMapping(path = "/notifications")
	public ResponseEntity<List<NotificationDto>> getNotificationsFromUser(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(notificationService.getAllNotificationsFromUser(token));
	}

	@Operation(description = "Realiza a visualização de uma notificação do usuário.")
	@PatchMapping(path = "/notifications/visualize/{id}")
	public void visualizeNotification(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
									  @RequestHeader("Authorization") String token) {
		notificationService.visualizeNotification(id, token);
	}

	@Operation(description = "Realiza a visualização de uma notificação do usuário.")
	@PatchMapping(path = "/notifications/visualize/all")
	public void visualizeAllNotification(@RequestHeader("Authorization") String token) {
		notificationService.visualizeAllNotifications(token);
	}

}
