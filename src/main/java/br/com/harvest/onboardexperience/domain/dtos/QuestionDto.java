package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("noteQuestion")
    private Long noteQuestion;

    @Builder.Default
    @JsonProperty("isActive")
    private Boolean isActive = true;

    @Builder.Default
    @JsonProperty("isMultipleChoice")
    private Boolean isMultipleChoice = false;

    @JsonProperty("answers")
    private List<AnswerQuestionDto> answers;

}

