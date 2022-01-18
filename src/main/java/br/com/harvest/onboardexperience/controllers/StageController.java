package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.services.StageService;
import br.com.harvest.onboardexperience.usecases.UserStageUseCase;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import javax.validation.constraints.Pattern;
import java.util.List;

@Tag(name = "Stage")
@RestController
@RequestMapping("/v1/stages")
@CrossOrigin(origins = "*", maxAge = 360000)
public class StageController {

    @Autowired
    private StageService service;

    @Autowired
    private UserStageUseCase usecase;

    @Operation(description = "Retorna Etapas Cadastradas.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StageDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.findAllByTenant(pageable,token));
    }

    @Operation(description = "Retorna a etapa cadastrada pelo ID.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StageDto> findById(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.findByid(id, token));
    }

    @Operation(description = "Salva uma Etapa no Banco de dados")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<StageDto> create(@Valid @ModelAttribute @NotNull StageDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(service.create(dto, file, token));
    }

    @Operation(description = "Realiza a alteração de uma Etapa no banco de dados e a retorna atualizada.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StageDto> update(@PathVariable @Pattern(regexp = RegexUtils.PASSWORD_VALIDATION)Long id, @Valid @ModelAttribute @NotNull StageDto dto, @RequestParam(value = "file",required = false) MultipartFile file, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(service.update(id,dto,file,token));
    }

    @Operation(description = "Realiza a exclusão de uma Etapa no banco de dados.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
        service.delete(id, token);
    }

    @Operation(description = "Realiza a inativação de uma moeda no banco de dados.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(path = "/disable/{id}")
    public void disable(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
        service.disableStage(id, token);
    }
    @Operation(description = "Deixar a Etapa criada disponivel para demais trilhas.")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StageDto>> findByStageAvailable (@Valid @ModelAttribute @NotNull StageDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token)throws RuntimeException {
        return ResponseEntity.ok().body(usecase.findAllStagesAvailables(dto,file,token));
    }

}
