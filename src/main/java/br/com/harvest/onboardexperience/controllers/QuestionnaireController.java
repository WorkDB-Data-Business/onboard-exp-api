package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.QuestionnaireDto;
import br.com.harvest.onboardexperience.services.AnswerQuestionService;
import br.com.harvest.onboardexperience.services.QuestionService;
import br.com.harvest.onboardexperience.services.QuestionnaireService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Tag(name = "Questionnaire")
@RestController
@CrossOrigin(origins = "*", maxAge = 360000)
@RequestMapping("/v1/questionnaires")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerQuestionService answerQuestionService;

    @Operation(description = "Criar um Questionario")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionnaireDto> createQuestionnaire(@Valid @RequestBody @NotNull QuestionnaireDto dto, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionnaireService.createQuestionnaire(dto,token));
    }

    @Operation(description = "Faz upload de uma image preview do questionário.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/{id}/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionnaireDto> uploadImagePreview(@PathVariable("id") Long id,
                                                               @RequestParam("imagePreviewFile") MultipartFile imagePreviewFile) throws RuntimeException{
        return ResponseEntity.ok().body(questionnaireService.uploadImagePreview(id, imagePreviewFile));
    }

    @Operation(description = "Retorna a listagem de questionários")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<QuestionnaireDto>> findAllQuestionnaire(@RequestHeader("Authorization") String token, Pageable pageable) throws RuntimeException{
        return ResponseEntity.ok().body(questionnaireService.findAllQuestionnaire(token, pageable));
    }

    @Operation(description = "Retorna o questionário por ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireDto> findQuestionnaireById(@PathVariable Long id, @RequestHeader("Authorization") String token) throws RuntimeException{
        return ResponseEntity.ok().body(questionnaireService.findQuestionnaireByIdAsDTO(id, token));
    }

    @Operation(description = "Realiza alteração do Questionario.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "/{idQuestionnaire}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionnaireDto> updateQuestionnaire(@PathVariable(name="idQuestionnaire") @Pattern(regexp = RegexUtils.ONLY_NUMBERS)Long id,
                                                           @Valid @RequestBody @NotNull QuestionnaireDto dto,
                                                           @RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.ok(questionnaireService.update(id,dto,token));
    }

    @Operation(description = "Realiza exclusão do questionario.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteQuestionnaire(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS)Long id, @RequestHeader("Authorization") String token) throws Exception {
        questionnaireService.deleteQuestionnaire(id,token);
    }

    @Operation(description = "Realiza exclusão da questão.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/questions/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteQuestion(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) throws Exception {
        questionService.deleteQuestion(id);
    }

    @Operation(description = "Realiza exclusão da questão.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/questions/answers/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAnswer(@PathVariable @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id) {
        answerQuestionService.deleteById(id);
    }
}