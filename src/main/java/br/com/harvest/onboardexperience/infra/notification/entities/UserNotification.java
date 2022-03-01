package br.com.harvest.onboardexperience.infra.notification.entities;


import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbnotification_user")
public class UserNotification {

    @EmbeddedId
    private UserNotificationKey id;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("idNotification")
    @JoinColumn(name = "idnotification")
    private Notification notification;

    @Column(name = "was_visualized")
    private Boolean wasVisualized;

}
