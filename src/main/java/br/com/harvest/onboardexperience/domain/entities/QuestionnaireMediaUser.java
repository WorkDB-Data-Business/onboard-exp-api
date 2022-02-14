package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.QuestionnaireMediaUserId;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(QuestionnaireMediaUserId.class)
@Entity(name="tbstage_questionnaire_user")
public class QuestionnaireMediaUser {

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "idquestionnaire"),
            @JoinColumn(name = "idstage")
    })
    private QuestionnaireMediaStage questionnaireMedia;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
