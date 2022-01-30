package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.Event;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEventDto {

    private Long id;

    private String name;

    private String descripton;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isMultipleChoice = false;

    private List<AnswerQuestionDto> answers;

}

