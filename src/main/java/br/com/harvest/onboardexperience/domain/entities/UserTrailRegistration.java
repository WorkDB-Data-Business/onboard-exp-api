package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.UserTrailRegistrationId;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserTrailRegistrationId.class)
@Entity(name = "tbtrail_user_registration")
@EqualsAndHashCode
public class UserTrailRegistration {

    @Id
    @ManyToOne
    @JoinColumn(name = "idtrail")
    private Trail trail;

    @Id
    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "started_trail_date")
    private LocalDateTime startedTrailDate;

    @Column(name = "finished_trail_date")
    private LocalDateTime finishedTrailDate;

}
