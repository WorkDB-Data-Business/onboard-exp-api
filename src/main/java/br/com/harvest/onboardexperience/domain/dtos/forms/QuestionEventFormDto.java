package br.com.harvest.onboardexperience.domain.dtos.forms;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEventFormDto {

    @NotNull
    private String name;

    @NotNull
    private String descripton;

    @Builder.Default
    private Boolean isActive = true;

    private Long noteQuestion;

    List<AnswerQuestionFormDto> answers;
}

