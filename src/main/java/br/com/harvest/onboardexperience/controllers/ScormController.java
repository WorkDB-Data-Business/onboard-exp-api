package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.exceptions.ScormCourseNotFoundException;

import br.com.harvest.onboardexperience.infra.scorm.services.ScormService;
import com.rusticisoftware.cloud.v2.client.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Scorm")
@RestController
@RequestMapping("/v1/scorm")
@CrossOrigin(origins = "*", maxAge = 36000)
public class ScormController {

    @Autowired
    private ScormService service;

    @Operation(description = "Realiza o registro do usuário com o curso.")
    @PostMapping(value = "/{scormID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerOnCourse(@PathVariable String scormID,
                                             @NonNull @RequestHeader("Authorization") String token) throws ApiException, ScormCourseNotFoundException {
        return ResponseEntity.ok(service.registerOnScormCourse(scormID, token));
    }

    @Operation(description = "Realiza a geração do link de execução do usuário com o curso.")
    @GetMapping(value = "/courses/{courseId}/launch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buildExecutionLinkCourse(@PathVariable("courseId") String courseId,
                                                 @NonNull @RequestHeader("Authorization") String token) throws ApiException, ScormCourseNotFoundException {
        return ResponseEntity.ok(service.generateScormExecutionLink(courseId, token));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Deleta o registro do usuário com o curso.")
    @DeleteMapping(value = "/{scormID}/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteRegistrationCourse(@PathVariable String scormID,
                                 @NonNull @RequestHeader("Authorization") String token) throws ApiException, ScormCourseNotFoundException {
        service.deleteRegistration(scormID, token);
    }
}
