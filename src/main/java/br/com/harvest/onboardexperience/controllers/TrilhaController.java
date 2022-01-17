package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.TrilhaDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrilhaForm;
import br.com.harvest.onboardexperience.services.TrilhaService;
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
@RequestMapping("/v1/trilha")
public class TrilhaController {

    @Autowired
    private TrilhaService trilhaService;

    @Operation(description = "Busca todas as trilhas e a retorna")
    @GetMapping
    public ResponseEntity<Page<TrilhaDTO>> buscaTodasTrilhas(Pageable pageable, @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(trilhaService.buscarTodasTrilhas(pageable,token));
    }

    @Operation(description = "Salva uma nova trilha")
    @PostMapping
    public ResponseEntity<TrilhaDTO> salvaTrilhaNova(@RequestPart("file")MultipartFile file,
                                                     @ModelAttribute("dto")TrilhaForm form,
                                                     @RequestHeader("Authorization")String token) throws IOException {
        return ResponseEntity.ok(this.trilhaService.salvarTrilhaNova(file,form,token));
    }

    @Operation(description = "Busca uma trilha pelo id dela e a retorna")
    @GetMapping(path = "/{idTrilha}")
    public ResponseEntity<TrilhaDTO> buscaTrilhaPorId(@PathVariable(name="idTrilha")Long idTrilha,
                                                      @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(this.trilhaService.buscarTrilhaPorId(idTrilha));
    }
    @Operation(description = "Deleta uma trilha")
    @DeleteMapping(path = "/{idTrilha}")
    public void deletarTrilha(@PathVariable(name="idTrilha")Long idTrilha,
                                                   @RequestHeader("Authorization")String token){
        this.trilhaService.deletarTrilha(idTrilha);
    }
}
