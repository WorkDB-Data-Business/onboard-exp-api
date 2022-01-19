package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.services.TrailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Trilha")
@RestController
@CrossOrigin("*")
@RequestMapping("/v1/trails")
public class TrailController {

    @Autowired
    private TrailService trailService;

    @Operation(description = "Busca todas as trilhas e a retorna")
    @GetMapping
    public ResponseEntity<Page<TrailDTO>> searchAllTrals(Pageable pageable, @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(trailService.searchAllTrals(pageable,token));
    }

    @Operation(description = "Salva uma nova trilha")
    @PostMapping
    public ResponseEntity<TrailDTO> saveTRail(@RequestPart("file")MultipartFile file,
                                                    @ModelAttribute("dto") TrailForm form,
                                                    @RequestHeader("Authorization")String token) throws IOException {
        return ResponseEntity.ok(this.trailService.saveTRail(file,form,token));
    }

    @Operation(description = "Busca uma trilha pelo id dela e a retorna")
    @GetMapping(path = "/{id_trail}")
    public ResponseEntity<TrailDTO> searchTrailId(@PathVariable(name="idTrilha")Long idTrail,
                                                     @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(this.trailService.searchTrailId(idTrail));
    }
    @Operation(description = "Deleta uma trilha")
    @DeleteMapping(path = "/{id_trail}")
    public void deleteTrail(@PathVariable(name="idTrilha")Long idTrail,
                                                   @RequestHeader("Authorization")String token){
        this.trailService.deleteTrail(idTrail);
    }

}
