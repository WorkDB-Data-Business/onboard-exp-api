package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.QuestionnaireMediaStageId;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(QuestionnaireMediaStageId.class)
@Entity(name="tbstage_questionnaire")
public class QuestionnaireMediaStage extends BaseEntityAudit {

    @Id
    @ManyToOne
    @JoinColumn(name = "idquestionnaire")
    private Questionnaire questionnaire;

    @Id
    @ManyToOne
    @JoinColumn(name = "idstage")
    private Stage stage;

}
