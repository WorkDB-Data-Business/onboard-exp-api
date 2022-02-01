package br.com.harvest.onboardexperience.domain.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerQuestionDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("isCorrect")
    @Builder.Default
    private Boolean isCorrect = false;

    @JsonProperty("idQuestion")
    private Long idQuestion;
}
