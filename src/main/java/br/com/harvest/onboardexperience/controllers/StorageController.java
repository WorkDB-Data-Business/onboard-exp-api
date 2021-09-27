package br.com.harvest.onboardexperience.controllers;

import java.io.IOException;
import java.util.List;

import br.com.harvest.onboardexperience.infra.storage.dtos.LinkForm;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.services.StorageAdapter;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;


@Tag(name = "Files")
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 36000)
public class StorageController {

	@Autowired
	private StorageAdapter storageAdapter;

	@GetMapping("/links")
	public ResponseEntity<Page<?>> findAllLinks(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(storageAdapter.setStorage(Storage.LINK).findAll(token, pageable));
	}

	@GetMapping("/files")
	public ResponseEntity<Page<?>> findAllFiles(Pageable pageable, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok().body(storageAdapter.setStorage(Storage.FILE).findAll(token, pageable));
	}

	@DeleteMapping("/{id}/{type}")
	public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
					   @PathVariable  Storage type,
					   @RequestHeader("Authorization") String token) {
		storageAdapter.setStorage(type).delete(id, token);
	}

	@PostMapping(value="/upload")
	public void upload(@ModelAttribute @Valid LinkForm dto,
					   @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
					   @RequestParam(value = "file", required = false) MultipartFile file,
					   @RequestHeader("Authorization") String token)
			throws IOException {
		storageAdapter.setForm(dto, file, authorizedClients, token).save();
	}


	@PutMapping(value="/upload/{id}")
	public void upload(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
					   @ModelAttribute @Valid LinkForm dto,
					   @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
					   @RequestParam(value = "file", required = false) MultipartFile file,
					   @RequestHeader("Authorization") String token)
			throws IOException {
		storageAdapter.setForm(dto, file, authorizedClients, token).update(id);
	}


}
