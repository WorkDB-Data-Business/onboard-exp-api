package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.LinkMediaUserId;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(LinkMediaUserId.class)
@Entity(name="tbstage_link_user")
public class LinkMediaUser {

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "idlink"),
            @JoinColumn(name = "idstage")
    })
    private LinkMediaStage linkMedia;

    @Id
    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
