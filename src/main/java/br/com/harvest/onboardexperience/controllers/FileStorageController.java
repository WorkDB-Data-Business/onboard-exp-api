package br.com.harvest.onboardexperience.controllers;

import java.io.IOException;

import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.services.FileStorageService;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Files")
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 36000)
public class FileStorageController {

	@Autowired
	private FileStorageService storageService;

	@Autowired
	private JwtTokenUtils jwtUtils;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/files/{filename:.+}/{type}")
	public ResponseEntity<String> getFile(@PathVariable String filename, @PathVariable FileTypeEnum type, @RequestHeader("Authorization") String token) throws IOException {
		UserDto user = userService.findByIdAndTenant(jwtUtils.getUserId(token), token);
		
		Resource file = storageService.load(filename, new String[] {user.getClient().getCnpj(), type.getName()});
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(Base64.encodeBase64String(FileUtils.readFileToByteArray(file.getFile())));
	}

}
