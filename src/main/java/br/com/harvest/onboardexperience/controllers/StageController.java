package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.StageDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.StageForm;
import br.com.harvest.onboardexperience.services.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Operation(description = "Retorna Etapas Cadastradas.")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Page<StageDTO>> findAll(Pageable pageable, @RequestHeader("Authorization") String token){
//        return ResponseEntity.ok(service.findAllByTenant(pageable,token));
//    }
//
//    @Operation(description = "Retorna a etapa cadastrada pelo ID.")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<StageDTO> findById(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token){
//        return ResponseEntity.ok(service.findByid(id, token));
//    }

    @Operation(description = "Salva uma Etapa no Banco de dados")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<StageDTO> create(@Valid @RequestBody StageForm dto, @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok().body(service.create(dto, token));
    }

    @Operation(description = "Busca uma etapa no banco de dados como admin.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/trails/{trailId}/stages/{stageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StageDTO> findAsAdmin(@PathVariable("trailId") Long trailId,
                                          @PathVariable("stageId") Long stageId,
                                          @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.findAsAdmin(trailId, stageId, token));
    }

    @Operation(description = "Busca uma etapa no banco de dados como colaborador.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @GetMapping(value = "/trails/{trailId}/stages/{stageId}/my-stage", produces = MediaType.APPLICATION_JSON_VALUE)
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

//    @Operation(description = "Realiza a exclusão de uma Etapa no banco de dados.")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping(path = "/{id}")
//    public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
//        service.delete(id, token);
//    }
//
//    @Operation(description = "Realiza a inativação de uma moeda no banco de dados.")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PatchMapping(path = "/disable/{id}")
//    public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
//        service.disableStage(id, token);
//    }
//
//    @Operation(description = "Retorna com todas as etapas disponiveis.")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping(path ="/stagesavailables", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<StageDTO>> findByStageAvailable (StageDTO dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token)throws RuntimeException {
//        return ResponseEntity.ok().body(usecase.findAllStagesAvailables(dto,file,token));
//    }
//    @Operation(description = "Cria um evento dentro da Etapa.")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PostMapping(path ="/event", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<EventDto> create(@Valid @ModelAttribute @NotNull EventDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
//        return ResponseEntity.ok().body(eventService.create(dto,file,token));
//    }
//
//    @Operation(description = "Realiza a conclusão da etapa")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('ADMIN' or hasAuthority('COLABORATOR'))")
//    @PostMapping(path = "/completestage", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void completeStage(@Valid @RequestBody UserCoinForm form, @RequestHeader("Authorization") String token){
//        usecase.completeStage(form,token);
//
//    }
//

}
