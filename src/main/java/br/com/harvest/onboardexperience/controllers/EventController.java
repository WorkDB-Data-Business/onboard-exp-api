package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.EventDto;
import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.services.EventService;
import br.com.harvest.onboardexperience.services.TrailService;
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

@Tag(name = "Event")
@RestController
@CrossOrigin(origins = "*", maxAge = 360000)
@RequestMapping("/v1/events")
public class EventController {

    @Autowired
    private TrailService trailService;

    @Autowired
    private EventService eventService;


    @Operation(description = "Retorna eventos cadastrados.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EventDto>> searchEvents(Pageable pageable, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(eventService.findAllByTenant(pageable,token));
    }

    @Operation(description = "Retorna os Eventos pelo ID.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> searchEventId(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(eventService.searchEventId(id,token));
    }

    @Operation(description = "Cria um evento dentro da Etapa.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> create(@Valid @ModelAttribute @NotNull EventDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(eventService.create(dto,file,token));
    }

    @Operation(description = "Realiza alteração de um evento.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> update(@PathVariable @Pattern(regexp = RegexUtils.PASSWORD_VALIDATION)Long id, @Valid @ModelAttribute @NotNull EventDto dto, @RequestParam(value = "file",required = false) MultipartFile file, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(eventService.update(id,dto,file,token));
    }

    @Operation(description = "Realiza a exclusão de um evento .")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
       eventService.delete(id,token);
    }




}