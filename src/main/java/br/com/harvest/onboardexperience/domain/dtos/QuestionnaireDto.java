package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import br.com.harvest.onboardexperience.domain.entities.Question;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionnaireDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("previewImagePath")
    private String previewImagePath;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty("isActive")
    private Boolean isActive = true;

    @JsonProperty("questions")
    private List<QuestionDto> questionOfQuestionnaire;

    @JsonProperty("authorizedClientsId")
    private List<Long> authorizedClientsId;

}

