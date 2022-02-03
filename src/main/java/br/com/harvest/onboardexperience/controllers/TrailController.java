package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.services.TrailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
//
//    @Autowired
//    private UserStageUseCase usecase;

//    @Autowired
//    private StageService stageService;
//
//    @Operation(description = "Busca todas as trilhas.")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping
//    public ResponseEntity<Page<TrailDTO>> searchAllTrals(Pageable pageable, @RequestHeader("Authorization")String token){
//        return ResponseEntity.ok(trailService.searchAllTrals(pageable,token));
//    }

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

    @Operation(description = "Busca uma trilha pelo id dela e a retorna")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public void searchTrailId(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        this.trailService.delete(id, token);
    }

    @Operation(description = "Atualiza uma trilha")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrailDTO> getById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.trailService.findTrailByIdAndEndUserByToken(id, token));
    }

//    @Operation(description = "Atualiza uma trilha")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COLABORATOR')")
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<TrailDTO> getById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
//        return ResponseEntity.ok(this.trailService.findTrailByIdAndEndUserByToken(id, token));
//    }


//    @Operation(description = "Retorna com todas as etapas disponiveis.")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping(path ="/stagesavailables", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<StageDto>> searchStageAvailable (StageDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token)throws RuntimeException {
//
//        return ResponseEntity.ok().body(usecase.findAllStagesAvailables(dto,file,token));
//    }
//
//    @Operation(description = "Criar uma etapa.")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public  ResponseEntity<StageDto> createNewStage(@Valid @ModelAttribute @NotNull StageDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
//        return ResponseEntity.ok().body(stageService.create(dto, file, token));
//    }
//

}