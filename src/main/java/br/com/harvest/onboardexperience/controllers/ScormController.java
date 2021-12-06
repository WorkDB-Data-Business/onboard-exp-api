package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.infra.scorm.dtos.ScormDto;
import br.com.harvest.onboardexperience.infra.scorm.services.ScormService;
import com.rusticisoftware.cloud.v2.client.ApiException;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Scorm")
@RestController
@RequestMapping("/v1/scorm")
@CrossOrigin(origins = "*", maxAge = 36000)
public class ScormController {

    @Autowired
    private ScormService service;

    @Operation(description = "Faz upload do curso SCORM.")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MASTER')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScormDto> upload(@NonNull @RequestParam("file") MultipartFile file,
                                           @RequestHeader("Authorization") String token) throws IOException, ApiException {
        return ResponseEntity.ok(service.importScormCourse(file, token));
    }
}
