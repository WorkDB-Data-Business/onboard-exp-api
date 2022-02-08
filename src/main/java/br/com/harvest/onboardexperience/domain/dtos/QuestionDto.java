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

    @JsonProperty("previewImagePath")
    private String previewImagePath;

    @JsonProperty("scoreQuestion")
    private Long scoreQuestion;

    @JsonProperty("isActive")
    private Boolean isActive ;

    @JsonProperty("isMultipleChoice")
    private Boolean isMultipleChoice;

    @JsonProperty("answers")
    private List<AnswerQuestionDto> answersQuestions;

    @JsonProperty("authorizedClients")
    private List<Long> authorizedClientsId;

}

