package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.services.AnswerQuestionService;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Tag(name = "Respostas do Questionário")
@RestController
@RequestMapping("/v1/questions")
@CrossOrigin("*")
public class AnswerQuestionController {

    @Autowired
    private AnswerQuestionService answerQuestionService;


    @Operation(description = "Coloca opções de respotas")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path ="/answers/options/{idQuestion}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerQuestionDto> putQuestionsOptions(@PathVariable(name = "idQuestion")Long idQuestion,
                                                                 @Valid @RequestBody @NotNull AnswerQuestionDto dto,
                                                                 @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok().body(answerQuestionService.putQuestionsOptions(idQuestion,dto,token));
    }

    @Operation(description = "Resposta do colaborador.")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(path ="/answers/multiple/{idQuestion}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerQuestionDto> answerQuestion(@PathVariable(name="idQuestion")Long idQuestion,
                                                            @Valid @ModelAttribute @NotNull AnswerQuestionDto dto,
                                                            @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(answerQuestionService.answerQuestion(idQuestion,dto,token));
    }

    @Operation(description = "Deleta uma opção do questionário")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/answers/{idAnswer}")
    public void deleteAnswerQuestionOption(@PathVariable(name = "idQuestion")Long idAnswer){
        this.answerQuestionService.deleteById(idAnswer);
    }

}
