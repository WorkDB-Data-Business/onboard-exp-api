package br.com.harvest.onboardexperience.domain.dtos;


import br.com.harvest.onboardexperience.domain.entities.Client;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerQuestionDto {

    private Long id;

    private String answer;

    private Boolean iscorrect = false;
}
