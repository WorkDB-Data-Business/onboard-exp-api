package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.LinkMediaUserId;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(LinkMediaUserId.class)
@Entity(name="tbstage_link_user")
public class LinkMediaUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "idlink")
    private Link link;

    @Id
    @ManyToOne
    @JoinColumn(name = "idstage")
    private Stage stage;

    @Id
    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
