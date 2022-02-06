package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.TextEventDto;
import br.com.harvest.onboardexperience.services.TextEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "text")
@RestController
@CrossOrigin(origins = "*", maxAge = 360000)
@RequestMapping("/v1/text")
public class TextEventController {

    @Autowired
    private TextEventService  textEventService;

    @Operation(description = "Inserir texto a ser Lido")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextEventDto> createText(@Valid @ModelAttribute @NotNull TextEventDto dto,
                                                   @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(textEventService.createText(dto,token));
    }




}