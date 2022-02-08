package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.TextDto;
import br.com.harvest.onboardexperience.services.EventService;
import br.com.harvest.onboardexperience.services.TextService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Tag(name = "text")
@RestController
@CrossOrigin(origins = "*", maxAge = 360000)
@RequestMapping("/v1/text")
public class TextController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TextService textService;

    @Operation(description = "Salva um Texto no banco de dados.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextDto> create(@Valid @RequestBody @NotNull TextDto dto,
                                              @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(textService.create(dto,token));
    }

    @Operation(description = "Rertona a listagem dos textos.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TextDto>> findAllText(@RequestHeader("Authorization") String token, Pageable pageable)
            throws RuntimeException{
        return ResponseEntity.ok().body(textService.findALltext(token,pageable));
    }

    @Operation(description = "Retorna o texto pelo ID.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextDto> findById(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(this.textService.findById(id));
    }

    @Operation(description = "Realiza alteração do texto.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextDto> update(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                           @Valid @RequestBody TextDto dto,
                                           @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok().body(textService.update(id,dto,token));
    }

    @Operation(description = "Realiza esclusão do texto no bandco de dados.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                           @RequestHeader("Authorization") String token) throws RuntimeException{
         textService.delete(id);
    }


}