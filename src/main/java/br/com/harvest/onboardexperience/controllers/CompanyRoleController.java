package br.com.harvest.onboardexperience.controllers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.domain.dto.CompanyRoleDto;
import br.com.harvest.onboardexperience.services.CompanyRoleService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Company Role")
@RestController
@RequestMapping("/v1")
public class CompanyRoleController {
	
	@Autowired
	private CompanyRoleService service;
	
	@Operation(description = "Retorna os cargos cadastrados.")
	@GetMapping(path = "/company-roles", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CompanyRoleDto>> findAll(Pageable pageable) {
		return ResponseEntity.ok(service.findAll(pageable));
	}
	
	@Operation(description = "Retorna o cargo cadastrado pelo ID.")
	@PreAuthorize(value = "isAuthenticated()")
	@GetMapping(path = "/company-roles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanyRoleDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(description = "Salva um cargo no banco de dados e o retorna.")
	@PreAuthorize(value = "isAuthenticated()")
	@PostMapping(path = "/company-roles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanyRoleDto> create(@RequestBody @NotNull CompanyRoleDto dto) {
		return ResponseEntity.ok().body(service.create(dto));
	}
	
	@Operation(description = "Realiza a alteração de um cargo no banco de dados e o retorna atualizado.")
	@PreAuthorize(value = "isAuthenticated()")
	@PutMapping(path = "/company-roles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanyRoleDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @NotNull CompanyRoleDto dto) {
		return ResponseEntity.ok().body(service.update(id, dto));
	}
	
	@Operation(description = "Realiza a exclusão de um cargo no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize(value = "isAuthenticated()")
	@DeleteMapping(path = "/company-roles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
		service.delete(id);
	}


}
