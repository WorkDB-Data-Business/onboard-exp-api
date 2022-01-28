package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.exceptions.ScormCourseNotFoundException;
import br.com.harvest.onboardexperience.infra.scorm.dtos.ScormDto;
import br.com.harvest.onboardexperience.infra.scorm.filters.ScormCourseFilter;
import br.com.harvest.onboardexperience.infra.scorm.services.ScormService;
import com.rusticisoftware.cloud.v2.client.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "Scorm")
@RestController
@RequestMapping("/v1/scorm")
@CrossOrigin(origins = "*", maxAge = 36000)
public class ScormController {

    @Autowired
    private ScormService service;

//    @Operation(description = "Faz upload do curso SCORM.")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MASTER')")
//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ScormDto> uploadCourse(@NonNull @RequestParam("file") MultipartFile file,
//                                           @RequestHeader("Authorization") String token) throws IOException, ApiException {
//        return ResponseEntity.ok(service.importScormCourse(file, token));
//    }

//    @Operation(description = "Retorna todos os cursos SCORM.")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MASTER')")
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Page<ScormDto>> findAll(Pageable pageable,
//                                                  @Valid ScormCourseFilter filter,
//                                                  @RequestHeader("Authorization") String token) throws IOException, ApiException {
//        return ResponseEntity.ok(service.findAll(pageable, filter, token));
//    }

//    @Operation(description = "Deleta o curso SCORM.")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MASTER')")
//    @DeleteMapping(value = "/{scormID}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void deleteCourse(@PathVariable String scormID,
//                                                 @RequestHeader("Authorization") String token) throws ScormCourseNotFoundException, ApiException {
//        service.deleteScormCourse(scormID, token);
//    }

    @Operation(description = "Realiza o registro do usuário com o curso.")
    @PostMapping(value = "/{scormID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerOnCourse(@PathVariable String scormID,
                                             @NonNull @RequestHeader("Authorization") String token) throws ApiException, ScormCourseNotFoundException {
        return ResponseEntity.ok(service.registerOnScormCourse(scormID, token));
    }

    @Operation(description = "Realiza a geração do link de execução do usuário com o curso.")
    @GetMapping(value = "/courses/{courseId}/registrations/{registrationID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buildExecutionLinkCourse(@PathVariable("courseId") String courseId,
                                                           @PathVariable("registrationID") String registrationID,
                                                 @NonNull @RequestHeader("Authorization") String token) throws ApiException, ScormCourseNotFoundException {
        return ResponseEntity.ok(service.generateScormExecutionLink(courseId, registrationID, token));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Deleta o registro do usuário com o curso.")
    @DeleteMapping(value = "/{scormID}/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteRegistrationCourse(@PathVariable String scormID,
                                 @NonNull @RequestHeader("Authorization") String token) throws ApiException, ScormCourseNotFoundException {
        service.deleteRegistration(scormID, token);
    }
}
