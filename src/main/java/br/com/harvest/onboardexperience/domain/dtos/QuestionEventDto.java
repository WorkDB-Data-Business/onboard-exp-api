package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.Event;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEventDto {

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

