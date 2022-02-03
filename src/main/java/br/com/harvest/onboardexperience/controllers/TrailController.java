package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.TrailSimpleDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.infra.storage.filters.CustomFilter;
import br.com.harvest.onboardexperience.services.TrailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Tag(name = "Trail")
@RestController
@CrossOrigin("*")
@RequestMapping("/v1/trails")
public class TrailController {

    @Autowired
    private TrailService trailService;

    @Operation(description = "Salva uma nova trilha")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrailDTO> create(@RequestParam(value = "mapImage") MultipartFile mapImage,
                                           @RequestParam(value = "mapMusic", required = false) MultipartFile mapMusic,
                                           @Valid @ModelAttribute TrailForm form,
                                           @RequestPart("characterMapPositionPath") List<PositionForm> characterMapPositionPath,
                                           @RequestHeader("Authorization") String token) throws IOException {
        return ResponseEntity.ok(this.trailService.save(form, characterMapPositionPath, mapImage, mapMusic, token));
    }

    @Operation(description = "Atualiza uma trilha")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrailDTO> update(@PathVariable("id") Long id,
                                           @RequestParam(value = "mapImage", required = false) MultipartFile mapImage,
                                           @RequestParam(value = "mapMusic", required = false) MultipartFile mapMusic,
                                           @Valid @ModelAttribute TrailForm form,
                                           @RequestPart("characterMapPositionPath") List<PositionForm> characterMapPositionPath,
                                           @RequestHeader("Authorization") String token) throws IOException {
        return ResponseEntity.ok(this.trailService.update(id, form, characterMapPositionPath, mapImage, mapMusic, token));
    }

    @Operation(description = "Realiza a remoção lógica da trilha no banco de dados.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        this.trailService.delete(id, token);
    }

    @Operation(description = "Busca uma trilha pelo id.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrailDTO> getByIdAdmin(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.trailService.findTrailByIdAndEndUserByTokenAsAdmin(id, token));
    }

    @Operation(description = "Busca uma trilha pelo id.")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR')")
    @GetMapping(value = "/{id}/my-trail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrailDTO> getByIdColaborator(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.trailService.findTrailByIdAndEndUserByTokenAsColaborator(id, token));
    }

    @Operation(description = "Busca todas as trilhas disponíveis no cliente.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TrailSimpleDTO>> findAll(Pageable pageable, CustomFilter customFilter, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.trailService.findAll(pageable, customFilter, token));
    }

    @Operation(description = "Busca todas as trilhas disponíveis no cliente.")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR')")
    @GetMapping(value = "/my-trails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TrailSimpleDTO>> findAllMyTrails(Pageable pageable, CustomFilter customFilter, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.trailService.findAllMyTrails(pageable, customFilter, token));
    }

}