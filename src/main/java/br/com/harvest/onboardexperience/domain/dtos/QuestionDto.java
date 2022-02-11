package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("descriptionQuestion")
    private String description;

    @JsonProperty("scoreQuestion")
    private BigDecimal scoreQuestion;

    @JsonProperty("isMultipleChoice")
    private Boolean isMultipleChoice;

    @JsonProperty("answersQuestions")
    private List<AnswerQuestionDto> answersQuestions;

}

