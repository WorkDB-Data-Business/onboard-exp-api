package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.services.AnswerQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "AnswerQuestion")
@RestController
@RequestMapping("/v1/answersquestion")
@CrossOrigin(origins = "*", maxAge = 360000)
public class AnswerQuestionController {

    @Autowired
    AnswerQuestionService service;

    @Operation(description = "Coloca opções de respotas")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/v1/questions/option", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerQuestionDto> optionAnswer(@Valid @ModelAttribute @NotNull AnswerQuestionDto dto,
                                                          @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(service.optionAnswer(dto,token));
    }

    @Operation(description = "Resposta do colaborador.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(path ="/v1/questions/answerquestion", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerQuestionDto> answerQuestion(@Valid @ModelAttribute @NotNull AnswerQuestionDto dto,
                                                          @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(service.answerQuestion(dto,token));
    }


}
