package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.StageUserId;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StageUserId.class)
@Entity(name = "tbstage_user")
public class StageUser {

    @Id
    @ManyToOne
    @Column(name = "iduser")
    private User user;

    @Id
    @ManyToOne
    @Column(name = "idstage")
    private Stage stage;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

}
