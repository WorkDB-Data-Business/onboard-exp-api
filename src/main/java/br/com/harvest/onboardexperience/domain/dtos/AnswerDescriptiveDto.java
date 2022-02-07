package br.com.harvest.onboardexperience.domain.dtos;


import br.com.harvest.onboardexperience.domain.entities.Question;
import br.com.harvest.onboardexperience.domain.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDescriptiveDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("answer_descriptive")
    private String answerDescriptive;

    @JsonProperty("questionId")
    private Long questionId;

}
