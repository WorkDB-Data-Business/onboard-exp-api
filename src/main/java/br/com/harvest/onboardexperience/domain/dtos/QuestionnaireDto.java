package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import br.com.harvest.onboardexperience.domain.entities.Question;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;

import java.math.BigDecimal;
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

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty("minimumScore")
    private BigDecimal minimumScore = BigDecimal.ZERO;

    @JsonProperty("questions")
    private List<QuestionDto> questionOfQuestionnaire;

    @JsonProperty("authorizedClientsId")
    private List<Long> authorizedClientsId;

    @JsonProperty("storage")
    private Storage storage;

}

