package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.repositories.StageRepository;
import br.com.harvest.onboardexperience.services.StageService;
import br.com.harvest.onboardexperience.services.TrailService;
import br.com.harvest.onboardexperience.usecases.UserStageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mapstruct.control.MappingControl;
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
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@Tag(name = "Trail")
@RestController
@CrossOrigin("*")
@RequestMapping("/v1/trails")
public class TrailController {

    @Autowired
    private TrailService trailService;

    @Autowired
    private UserStageUseCase usecase;

    @Autowired
    private StageService stageService;

    @Operation(description = "Busca todas as trilhas.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TrailDTO>> searchAllTrals(Pageable pageable, @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(trailService.searchAllTrals(pageable,token));
    }

    @Operation(description = "Salva uma nova trilha")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<TrailDTO> saveTrail(@RequestPart("file")MultipartFile file,
                                                    @ModelAttribute("dto") TrailForm form,
                                                    @RequestHeader("Authorization")String token) throws IOException {
        return ResponseEntity.ok(this.trailService.saveTrail(file,form,token));
    }

    @Operation(description = "Busca uma trilha pelo id dela e a retorna")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{id_trail}")
    public ResponseEntity<TrailDTO> searchTrailId(@PathVariable(name="idTrilha")Long idTrail,
                                                     @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(this.trailService.searchTrailId(idTrail));
    }

    @Operation(description = "Deleta uma trilha")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id_trail}")
    public void deleteTrail(@PathVariable(name="idTrilha")Long idTrail, @RequestHeader("Authorization")String token){
        this.trailService.deleteTrail(idTrail);
    }
    @Operation(description = "Retorna com todas as etapas disponiveis.")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path ="/stagesavailables", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StageDto>> searchStageAvailable (StageDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token)throws RuntimeException {

        return ResponseEntity.ok().body(usecase.findAllStagesAvailables(dto,file,token));
    }

    @Operation(description = "Criar uma etapa.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<StageDto> createNewStage(@Valid @ModelAttribute @NotNull StageDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(stageService.create(dto, file, token));
    }
}