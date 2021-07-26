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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.domain.dto.CoinDto;
import br.com.harvest.onboardexperience.services.CoinService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Coins")
@RestController
@RequestMapping("/v1")
public class CoinController {
	
	@Autowired
	private CoinService service;
	
	@Operation(description = "Retorna as moedas cadastradas.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/coins", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CoinDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(pageable, token));
	}	
	
	@Operation(description = "Retorna a moeda cadastrada pelo ID.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/coins/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CoinDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByIdAndTenant(id, token));
	}
	
	@Operation(description = "Salva uma moeda no banco de dados e a retorna.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = "/coins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CoinDto> create(@Valid @RequestBody @NotNull CoinDto dto, @RequestHeader("Authorization") String token) throws RuntimeException {
		return ResponseEntity.ok().body(service.create(dto, token));
	}
	
	@Operation(description = "Realiza a alteração de uma moeda na banco de dados e a retorna atualizada.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = "/coins/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CoinDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestBody @Valid @NotNull CoinDto dto, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.update(id, dto, token));
	}
	
	@Operation(description = "Realiza a exclusão de uma moeda no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/coins/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.delete(id, token);
	}
	
}
