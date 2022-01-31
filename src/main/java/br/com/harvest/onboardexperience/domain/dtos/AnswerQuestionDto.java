package br.com.harvest.onboardexperience.domain.dtos;


import br.com.harvest.onboardexperience.domain.entities.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerQuestionDto {

    private Long id;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("isCorrect")
    @Builder.Default
    private Boolean isCorrect = false;


    private Long idQuestion;
}
