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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.harvest.onboardexperience.domain.dto.RewardDto;
import br.com.harvest.onboardexperience.services.RewardService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Rewards")
@RestController
@RequestMapping("/v1")
public class RewardController {
	
	@Autowired
	private RewardService service;
	
	@Operation(description = "Retorna as recompensas cadastradas.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/rewards", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<RewardDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(pageable, token));
	}	
	
	@Operation(description = "Retorna a recompensa cadastrada pelo ID.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/rewards/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RewardDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByIdAndTenant(id, token));
	}
	
	@Operation(description = "Salva uma recompensa no banco de dados e a retorna.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = "/rewards", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RewardDto> create(@Valid @ModelAttribute @NotNull RewardDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException {
		return ResponseEntity.ok().body(service.create(dto, file, token));
	}
	
	@Operation(description = "Realiza a alteração de uma recompensa no banco de dados e a retorna atualizada.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = "/rewards/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RewardDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @ModelAttribute @Valid @NotNull RewardDto dto, 
			@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.update(id, dto, file, token));
	}
	
	@Operation(description = "Realiza a exclusão de uma recompensa no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/rewards/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.delete(id, token);
	}
	
	@Operation(description = "Realiza a inativação de uma recompensa no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = "/rewards/disable/{id}")
	public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.disableReward(id, token);
	}

}