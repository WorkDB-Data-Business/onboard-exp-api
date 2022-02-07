package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.AnswerDescriptiveDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.domain.enumerators.QuestionFrom;
import br.com.harvest.onboardexperience.services.QuestionService;
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
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Operation(description = "Criar pergunta no questionario")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDto> createQuestion(@Valid @RequestBody @NotNull QuestionDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionService.createQuestion(dto,token));
    }

    @Operation(description = "Retorna a listagem de questionários")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<QuestionDto>> findAllQuestions(@RequestHeader("Authorization") String token, Pageable pageable) throws RuntimeException{
        return ResponseEntity.ok().body(questionService.findAllQuestions(token, pageable));
    }

    @Operation(description = "Retorna o questionário por ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/{from}")
    public ResponseEntity<QuestionDto> findQuestionById(@PathVariable(name = "from") QuestionFrom from, @PathVariable(name = "id") Long id,
                                                @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionService.findQuestionById(id,from));
    }


    @Operation(description = "Realiza alteração de uma pergunta.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{idQuestion}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDto> updateQuestionEvent(@PathVariable(name="idQuestion") @Pattern(regexp = RegexUtils.ONLY_NUMBERS)Long id,
                                                           @Valid @RequestBody @NotNull QuestionDto dto,
                                                           @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok(questionService.updateQuestionEvent(id,dto,token));
    }

    @Operation(description = "Realiza Exclusão da pergunta no questionario.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteQuestion(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS)Long id, @RequestHeader("Authorization") String token) throws Exception {
         questionService.deleteQuestion(id,token);
    }



}