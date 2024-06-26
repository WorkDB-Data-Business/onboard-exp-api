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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.services.ClientService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Clients")
@RestController
@RequestMapping("/v1/clients")
@CrossOrigin(origins = "*", maxAge = 36000)
public class ClientController {
	
	@Autowired
	private ClientService service;
	
	@Operation(description = "Retorna os clientes cadastrados.")
	@PreAuthorize("hasAuthority('MASTER')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ClientDto>> findAll(Pageable pageable) {
		return ResponseEntity.ok(service.findAll(pageable));
	}

	@Operation(description = "Retorna os clientes cadastrados.")
	@PreAuthorize("hasAuthority('MASTER')")
	@GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClientDto>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@Operation(description = "Retorna os cliente com base no valor buscado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/find/{criteria}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ClientDto>> findbyCriteria(@PathVariable String criteria, Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByCriteria(criteria, pageable, token));
	}
	
	@Operation(description = "Retorna o cliente cadastrado pelo ID.")
	@PreAuthorize("hasAuthority('MASTER')")
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(description = "Salva um cliente no banco de dados e o retorna.")
	@PreAuthorize("hasAuthority('MASTER')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientDto> create(@Valid @RequestBody @NotNull ClientDto dto) throws Exception {
		return ResponseEntity.ok().body(service.create(dto));
	}
	
	@Operation(description = "Realiza a alteração de um cliente no banco de dados e o retorna atualizado.")
	@PreAuthorize("hasAuthority('MASTER')")
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @Valid @NotNull ClientDto dto) {
		return ResponseEntity.ok().body(service.update(id, dto));
	}
	
	@Operation(description = "Realiza a exclusão de um cliente no banco de dados.")
	@PreAuthorize("hasAuthority('MASTER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.delete(id);
	}
	
	@Operation(description = "Realiza a inativação de um cliente no banco de dados.")
	@PreAuthorize("hasAuthority('MASTER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/disable/{id}")
	public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.disableClient(id);
	}
	
	@Operation(description = "Realiza a expiração de um cliente no banco de dados.")
	@PreAuthorize("hasAuthority('MASTER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/expire/{id}")
	public void expire(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.expireClient(id);
	}
	
	@Operation(description = "Realiza a  de um client no banco de dados.")
	@PreAuthorize("hasAuthority('MASTER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping(path = "/block/{id}")
	public void block(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.blockClient(id);
	}

}
