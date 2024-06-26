package br.com.harvest.onboardexperience.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.harvest.onboardexperience.domain.dtos.forms.CompanyRoleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.harvest.onboardexperience.domain.dtos.CompanyRoleDto;
import br.com.harvest.onboardexperience.services.CompanyRoleService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Company Role")
@RestController
@RequestMapping("/v1/company-roles")
@CrossOrigin(origins = "*", maxAge = 36000)
public class CompanyRoleController {
	
	@Autowired
	private CompanyRoleService service;
	
	@Operation(description = "Retorna os cargos cadastrados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CompanyRoleDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(pageable, token));
	}

	@Operation(description = "Retorna os cargos cadastrados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CompanyRoleDto>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(token));
	}

	@Operation(description = "Retorna os cargos com base no valor buscado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/find/{criteria}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CompanyRoleDto>> findbyCriteria(@PathVariable String criteria, Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByCriteria(criteria, pageable, token));
	}

	@Operation(description = "Retorna o cargo cadastrado pelo ID.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanyRoleDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByIdAndTenant(id, token));
	}
	
	@Operation(description = "Salva um cargo no banco de dados e o retorna.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanyRoleDto> create(@RequestBody @Valid @NotNull CompanyRoleForm dto, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.create(dto, token));
	}
	
	@Operation(description = "Realiza a alteração de um cargo no banco de dados e o retorna atualizado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanyRoleDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id
			, @RequestBody @Valid @NotNull CompanyRoleForm dto, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.update(id, dto, token));
	}
	
	@Operation(description = "Realiza a exclusão de um cargo no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.delete(id, token);
	}


}
