package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.domain.dtos.AnswerDescriptiveDto;
import br.com.harvest.onboardexperience.services.AnswerDescriptiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@Tag(name = "Respostas descritivas")
@RestController
@RequestMapping("/v1/questions/")
@CrossOrigin("*")
public class AnswerDescriptiveController {

    @Autowired
    private AnswerDescriptiveService answerDescriptiveService;


    @Operation(description = "Responde uma questão descritiva")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PostMapping(value = "/answers/descriptive/{idQuestion}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerDescriptiveDto> answerDescriptive(@PathVariable(name = "idQuestion")Long idQuestion,
                                                                  @RequestBody()AnswerDescriptiveDto dto,
                                                                  @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(this.answerDescriptiveService.answerDescriptive(idQuestion,dto,token));
    }

    @Operation(description = "Retorna todas as respostas descritivas ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "answers/descriptive/findall/{idQuestion}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AnswerDescriptiveDto>> findAllDescriptivesAnswers(@PathVariable(name = "idQuestion")Long idQuestion,
                                                                                 Pageable pageable,@RequestHeader("Authorization")String token){
        return ResponseEntity.ok(this.answerDescriptiveService.findAllDescriptivesAnswers(idQuestion,pageable));
    }

    @Operation(description = "Busca uma resposta descritiva por seu id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/answers/descriptive/find/{idAnswerDescriptive}")
    public ResponseEntity<AnswerDescriptiveDto> findDescriptiveAnswerById(@PathVariable(name = "idAnswerDescriptive")Long idAnswerDescriptive,
                                                                          @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(this.answerDescriptiveService.findById(idAnswerDescriptive));
    }

    @Operation(description = "Altera uma resposta descritiva")
    @PreAuthorize("hasAuthority('COLABORATOR')")
    @PutMapping(value = "/answers/descriptive/",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<AnswerDescriptiveDto> updateAnswerDescriptive(@RequestBody AnswerDescriptiveDto answerDescriptiveDto,
                                                                        @RequestHeader("Authorization")String token ){
        return ResponseEntity.ok(this.answerDescriptiveService.updateAnswerDescriptive(answerDescriptiveDto,token));
    }

    @Operation(description = "Deleta uma resposta descritiva")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/answers/descriptive/{idAnswerDescriptive}")
    public void deleteAnswerDescriptive(@PathVariable(name = "idAnswerDescriptive")Long idAnswerDescriptive,@RequestHeader("Authorization")String token){
        this.answerDescriptiveService.deleteById(idAnswerDescriptive);
    }
}
