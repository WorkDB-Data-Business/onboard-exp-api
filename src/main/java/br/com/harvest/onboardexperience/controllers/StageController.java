package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.StageDTO;
import br.com.harvest.onboardexperience.domain.dtos.StageUserDTO;
import br.com.harvest.onboardexperience.domain.dtos.StageUserSimpleDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.StageForm;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.services.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Stage")
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 360000)
public class StageController {

    @Autowired
    private StageService service;

    @Operation(description = "Salva uma Etapa no Banco de dados")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/trails/{trailId}/stages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<StageDTO> create(@PathVariable("trailId") Long trailId,
                                            @Valid @RequestBody StageForm dto,
                                            @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok().body(service.create(trailId, dto, token));
    }

    @Operation(description = "Atualiza uma etapa no Banco de dados")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/trails/{trailId}/stages/id/{stageId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<StageDTO> update(@PathVariable("trailId") Long trailId,
                                            @PathVariable("stageId") Long stageId,
                                            @Valid @RequestBody StageForm dto,
                                            @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok().body(service.update(trailId, stageId, dto, token));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Deleta uma etapa no Banco de dados")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/trails/{trailId}/stages/id/{stageId}")
    public void delete(@PathVariable("trailId") Long trailId,
                       @PathVariable("stageId") Long stageId,
                       @RequestHeader("Authorization") String token) throws Exception {
        service.delete(trailId, stageId, token);
    }

    @Operation(description = "Busca uma etapa no banco de dados como colaborador.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @GetMapping(value = "/trails/{trailId}/stages/id/{stageId}/my-stage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StageDTO> findAsColaborator(@PathVariable("trailId") Long trailId,
                                                      @PathVariable("stageId") Long stageId,
                                                      @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.findAsColaboratorAsDTO(trailId, stageId, token));
    }

    @Operation(description = "Busca uma etapa no banco de dados como admin.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/trails/{trailId}/stages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StageDTO>> findAllStagesByTrailAsAdmin(@PathVariable("trailId") Long trailId,
                                                                      @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.findAllByTrailAsAdmin(trailId, token));
    }

    @Operation(description = "Busca uma etapa no banco de dados como colaborador.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @GetMapping(value = "/trails/{trailId}/stages/my-stages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StageDTO>> findAllStagesByTrailAsColaborator(@PathVariable("trailId") Long trailId,
                                                               @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.findAllByTrailAsColaborator(trailId, token));
    }

    @Operation(description = "Busca uma etapa no banco de dados pela posição como colaborador.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @GetMapping(value = "/trails/{trailId}/stages/position", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StageDTO> findStageByPosition(@Valid PositionDTO position,
                                                        @PathVariable("trailId") Long trailId,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.findStageByPosition(trailId, position, token));
    }

    @Operation(description = "Busca uma etapa no banco de dados como admin.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/trails/{trailId}/stages/id/{stageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StageDTO> findAsAdmin(@PathVariable("trailId") Long trailId,
                                                @PathVariable("stageId") Long stageId,
                                                @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.findAsAdminAsDTO(trailId, stageId, token));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Registra a execução da mídia na etapa.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(value = "/trails/{trailId}/stages/{stageId}/media/{mediaId}/{mediaType}/start")
    public void startMedia(@PathVariable("trailId") Long trailId,
                                    @PathVariable("stageId") Long stageId,
                                    @PathVariable("mediaId") String mediaId,
                                    @PathVariable("mediaType") Storage type,
                                    @RequestHeader("Authorization") String token) throws Exception {
        service.startMedia(trailId, stageId, mediaId, type, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Registra a execução da etapa com o usuário.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(value = "/trails/{trailId}/stages/{stageId}/start")
    public void startStage(@PathVariable("trailId") Long trailId,
                           @PathVariable("stageId") Long stageId,
                           @RequestHeader("Authorization") String token) throws Exception {
        service.startStage(trailId, stageId, token);
    }

    @Operation(description = "Registra a finalização da etapa com o usuário.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(value = "/trails/{trailId}/stages/{stageId}/finish")
    public ResponseEntity<StageUserSimpleDTO> finishStage(@PathVariable("trailId") Long trailId,
                                                          @PathVariable("stageId") Long stageId,
                                                          @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.finishStage(trailId, stageId, token));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Registra a conclusão da execução da mídia na etapa.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(value = "/trails/{trailId}/stages/{stageId}/media/{mediaId}/{mediaType}/finish")
    public void finishMedia(@PathVariable("trailId") Long trailId,
                                    @PathVariable("stageId") Long stageId,
                                    @PathVariable("mediaId") String mediaId,
                                    @PathVariable("mediaType") Storage type,
                                    @RequestHeader("Authorization") String token) throws Exception {
        service.finishMedia(trailId, stageId, mediaId, type, token);
    }
}
