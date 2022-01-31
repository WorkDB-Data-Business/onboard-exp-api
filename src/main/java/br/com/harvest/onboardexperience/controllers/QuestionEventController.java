package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.QuestionEventFormDto;
import br.com.harvest.onboardexperience.services.EventService;
import br.com.harvest.onboardexperience.services.QuestionEventService;
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
    @PostMapping(path ="/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> createQuestion(@Valid @RequestBody @NotNull QuestionEventFormDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.createQuestion(dto,token));
    }

    @Operation(description = "Retorna a listagem de questionários")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<QuestionEventDto>> findAll(@RequestHeader("Authorization") String token, Pageable pageable) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.findAll(token, pageable    ));
    }

    @Operation(description = "Retorna o questionário por ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/{from}")
    public ResponseEntity<QuestionEventDto> findById(@PathVariable(name = "from")String from,@PathVariable(name = "id") Long id,
                                                     @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.findById(id,from));
    }

    @Operation(description = "Inserir nota da pergunta ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/note", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> noteQuestion(@NotNull QuestionEventDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.noteQuestion(dto,token));
    }

    @Operation(description = "Coloca opções de respotas")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/option/idQuestion", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerQuestionDto> optionAnswer(@PathVariable(name = "idQuestion")Long idQuestion,
                                                          @Valid @ModelAttribute @NotNull AnswerQuestionDto dto,
                                                          @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.optionAnswer(dto,token));
    }

    @Operation(description = "Resposta do colaborador.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(path ="/answerquestion/{idQuestion}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerQuestionDto> answerQuestion(@PathVariable(name="idQuestion")Long idQuestion,
                                                            @Valid @ModelAttribute @NotNull AnswerQuestionDto dto,
                                                            @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.answerQuestion(dto,token));
    }

    @Operation(description = "Realiza alteração do questionario.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEventDto> updateQuestionEvent(@PathVariable @Pattern(regexp = RegexUtils.PASSWORD_VALIDATION)Long id,
                                                                @Valid @RequestBody @NotNull QuestionEventFormDto dto,
                                                                @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionEventService.updateQuestionEvent(id,dto,token));
    }

    @Operation(description = "Realiza Exclusão do questionario.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable @Pattern(regexp = RegexUtils.PASSWORD_VALIDATION)Long id, @Valid @RequestBody @NotNull QuestionEventFormDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
         questionEventService.delete(id,dto,token);
    }

}