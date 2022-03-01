package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;

import br.com.harvest.onboardexperience.infra.storage.services.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@Tag(name = "Storage")
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 36000)
public class StorageController {

    @Autowired
    private FileStorageService service;

    @Operation(description = "Busca um arquivo pelo path.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/files/")
    public ResponseEntity<FileDto> findByContentPath(@RequestParam String contentPath) throws FileNotFoundException {
        return ResponseEntity.ok(service.find(contentPath));
    }

}
