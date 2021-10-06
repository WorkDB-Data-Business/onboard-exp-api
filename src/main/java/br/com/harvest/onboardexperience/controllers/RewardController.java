package br.com.harvest.onboardexperience.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.harvest.onboardexperience.domain.dtos.forms.RewardForm;
import br.com.harvest.onboardexperience.usecases.UserRewardUseCase;
import br.com.harvest.onboardexperience.usecases.forms.RewardPurchaseForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.harvest.onboardexperience.domain.dtos.RewardDto;
import br.com.harvest.onboardexperience.services.RewardService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Rewards")
@RestController
@RequestMapping("/v1/rewards")
@CrossOrigin(origins = "*", maxAge = 36000)
public class RewardController {
	
	@Autowired
	private RewardService service;

	@Autowired
	private UserRewardUseCase useCase;
	
	@Operation(description = "Retorna as recompensas cadastradas.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<RewardDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(pageable, token));
	}

	@Operation(description = "Retorna os recompensa com base no valor buscado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/find/{criteria}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<RewardDto>> findbyCriteria(@PathVariable String criteria, Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByCriteria(criteria, pageable, token));
	}
	
	@Operation(description = "Retorna a recompensa cadastrada pelo ID.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RewardDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findRewardDtoByIdAndTenant(id, token));
	}
	
	@Operation(description = "Salva uma recompensa no banco de dados e a retorna.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RewardDto> create(@Valid @ModelAttribute @NotNull RewardForm form, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException {
		return ResponseEntity.ok().body(service.create(form, file, token));
	}
	
	@Operation(description = "Realiza a alteração de uma recompensa no banco de dados e a retorna atualizada.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RewardDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
											@ModelAttribute @Valid @NotNull RewardForm form,
											@RequestParam(value= "file", required = false) MultipartFile file,
											@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.update(id, form, file, token));
	}
	
	@Operation(description = "Realiza a exclusão de uma recompensa no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.delete(id, token);
	}
	
	@Operation(description = "Realiza a inativação de uma recompensa no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = "/disable/{id}")
	public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.disableReward(id, token);
	}

	@Operation(description = "Realiza a compra de uma recompensa.")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR')")
	@PostMapping(path = "/purchase")
	public void purchaseReward(@Valid @RequestBody RewardPurchaseForm form, @RequestHeader("Authorization") String token) {
		useCase.purchaseReward(form, token);
	}

}
