package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.EventDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
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
    public ResponseEntity<QuestionEventDto> createQuestion(@Valid @ModelAttribute @NotNull QuestionEventDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.createQuestion(dto,file,token));
    }


    @Operation(description = "Coloca opções de respotas")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/v1/questions/option", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> optionAnswer(@Valid @ModelAttribute @NotNull QuestionEventDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.optionAnswer(dto,file,token));
    }

    @Operation(description = "Resposta do usuario ao questionario")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(path ="/v1/questions/answer", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> answerQuestion(@Valid @ModelAttribute @NotNull QuestionEventDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.answerQuestion(dto,file,token));
    }

    @Operation(description = "Inserir nova ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/v1/questions/note", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> noteQuestion(@Valid @ModelAttribute @NotNull QuestionEventDto dto, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.noteQuestion(dto,file,token));
    }











}