package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.GroupDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.GroupForm;
import br.com.harvest.onboardexperience.domain.dtos.GroupSimpleDto;
import br.com.harvest.onboardexperience.services.GroupService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Tag(name = "Group")
@RestController
@RequestMapping("/v1/groups")
@CrossOrigin(origins = "*", maxAge = 36000)
public class GroupController {

    @Autowired
    private GroupService service;

    @Operation(description = "Retorna os grupos cadastrados.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GroupSimpleDto>> findAll(Pageable pageable, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findAllByTenant(pageable, token));
    }

    @Operation(description = "Retorna o grupo cadastrado pelo ID.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupForm> findById(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByIdAndTenant(id, token));
    }

    @Operation(description = "Salva um grupo no banco de dados e o retorna.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDto> create(@RequestBody @Valid @NotNull GroupForm dto, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.create(dto, token));
    }

    @Operation(description = "Realiza a alteração de um grupo no banco de dados e o retorna atualizado.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDto> update(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id
            , @RequestBody @Valid @NotNull GroupForm dto,  @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(service.update(id, dto, token));
    }

    @Operation(description = "Realiza a exclusão de um grupo no banco de dados.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id, @RequestHeader("Authorization") String token) {
        service.delete(id, token);
    }
}
