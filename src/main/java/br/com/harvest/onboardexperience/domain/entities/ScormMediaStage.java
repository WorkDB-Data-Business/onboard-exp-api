package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.ScormMediaStageId;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(ScormMediaStageId.class)
@Entity(name="tbstage_scorm")
public class ScormMediaStage {

    @Id
    @ManyToOne
    @JoinColumn(name = "idscorm")
    private Scorm scorm;

    @Id
    @ManyToOne
    @JoinColumn(name = "idstage")
    private Stage stage;

    @OneToMany(mappedBy = "scormMedia")
    private List<ScormMediaUser> users;

}
