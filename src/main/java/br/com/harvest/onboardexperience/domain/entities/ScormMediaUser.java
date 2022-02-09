package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.ScormMediaUserId;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(ScormMediaUserId.class)
@Entity(name = "tbstage_scorm_user")
public class ScormMediaUser {

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "idscorm"),
            @JoinColumn(name = "idstage")
    })
    private ScormMediaStage scormMedia;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
