package br.com.harvest.onboardexperience.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.harvest.onboardexperience.domain.dtos.UserCoinDto;
import br.com.harvest.onboardexperience.usecases.UserCoinUseCase;
import br.com.harvest.onboardexperience.usecases.forms.UserCoinForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.harvest.onboardexperience.domain.dtos.CoinDto;
import br.com.harvest.onboardexperience.services.CoinService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Coins")
@RestController
@RequestMapping("/v1/coins")
@CrossOrigin(origins = "*", maxAge = 36000)
public class CoinController {
	
	@Autowired
	private CoinService service;

	@Autowired
	private UserCoinUseCase useCase;
	
	@Operation(description = "Retorna as moedas cadastradas.")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CoinDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(pageable, token));
	}

	@Operation(description = "Retorna as moedas cadastradas.")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR')")
	@GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CoinDto>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByTenant(token));
	}

	@Operation(description = "Retorna as moedas com base no valor buscado.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/find/{criteria}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CoinDto>> findbyCriteria(@PathVariable String criteria, Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByCriteria(criteria, pageable, token));
	}
	
	@Operation(description = "Retorna a moeda cadastrada pelo ID.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CoinDto> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findByIdAndTenant(id, token));
	}
	
	@Operation(description = "Salva uma moeda no banco de dados e a retorna.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CoinDto> create(@Valid @ModelAttribute @NotNull CoinDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException {
		return ResponseEntity.ok().body(service.create(dto, file, token));
	}
	
	@Operation(description = "Realiza a alteração de uma moeda no banco de dados e a retorna atualizada.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CoinDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @Valid @ModelAttribute @NotNull CoinDto dto, @RequestParam(value = "file", required = false) MultipartFile file, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(service.update(id, dto, file, token));
	}
	
	@Operation(description = "Realiza a exclusão de uma moeda no banco de dados.")
	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.delete(id, token);
	}
	
	@Operation(description = "Realiza a inativação de uma moeda no banco de dados.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = "/disable/{id}")
	public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
		service.disableCoin(id, token);
	}

	@Operation(description = "Realiza a adição de moedas para um usuário.")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/add-to-user")
	public void addToUser(@Valid @RequestBody UserCoinForm form, @RequestHeader("Authorization") String token) {
		useCase.addCoinToUser(form, token);
	}

	@Operation(description = "Realiza a subtração de moedas para um usuário.")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(path = "/subtract-from-user")
	public void subtractFromUser(@Valid @RequestBody UserCoinForm form, @RequestHeader("Authorization") String token) {
		useCase.subtractCoinFromUser(form, token);
	}

	@Operation(description = "Retorna a quantidade de todas as moedas que o usuário possui.")
	@GetMapping(path = "/coins-from-user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserCoinDto>> getAllCoinsFromUser(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(useCase.getAllCoinAmountFromUser(token));
	}
}
