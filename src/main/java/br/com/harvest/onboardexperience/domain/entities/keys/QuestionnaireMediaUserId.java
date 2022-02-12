package br.com.harvest.onboardexperience.domain.entities.keys;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionnaireMediaUserId implements Serializable {

    private QuestionnaireMediaStageId questionnaireMedia;

    private Long user;

}
