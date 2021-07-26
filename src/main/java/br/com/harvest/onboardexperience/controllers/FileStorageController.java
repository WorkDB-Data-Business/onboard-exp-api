package br.com.harvest.onboardexperience.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.services.FileStorageService;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Files")
@RestController
@RequestMapping("/v1")
public class FileStorageController {

	@Autowired
	private FileStorageService storageService;

	@Autowired
	private JwtTokenUtils jwtUtils;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename, @RequestHeader("Authorization") String token) {
		UserDto user = userService.findByIdAndTenant(jwtUtils.getUserId(token), token);
		
		Resource file = storageService.load(filename, user.getClient().getCnpj());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

}
