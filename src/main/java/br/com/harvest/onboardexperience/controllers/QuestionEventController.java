package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.EventDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.QuestionEventFormDto;
import br.com.harvest.onboardexperience.services.EventService;
import br.com.harvest.onboardexperience.services.QuestionEventService;
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
import java.util.List;

@Tag(name = "Question")
@RestController
@CrossOrigin(origins = "*", maxAge = 360000)
@RequestMapping("/v1/questions")
public class QuestionEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private QuestionEventService questionEventService;

    @Operation(description = "Criar pergunta no questionario")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> createQuestion(@Valid @RequestBody @NotNull QuestionEventFormDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.createQuestion(dto,token));
    }

    @Operation(description = "Retorna a listagem de questionários")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<QuestionEventDto>> findAll(@Valid @RequestBody @NotNull QuestionEventFormDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.findAll(token));
    }

    @Operation(description = "Retorna o questionário por ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionEventDto> findById(@PathVariable Long id, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.findById(id));
    }

    @Operation(description = "Inserir nota da pergunta ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/v1/questions/note", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> noteQuestion(@NotNull QuestionEventDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.noteQuestion(dto,token));
    }











}