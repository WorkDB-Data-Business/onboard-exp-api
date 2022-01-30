package br.com.harvest.onboardexperience.domain.dtos.forms;


import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerQuestionFormDto {

    private Long id;

    private String answer;

    private Boolean iscorrect;
}
