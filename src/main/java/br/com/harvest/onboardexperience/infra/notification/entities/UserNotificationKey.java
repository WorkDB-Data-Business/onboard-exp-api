package br.com.harvest.onboardexperience.infra.notification.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class UserNotificationKey implements Serializable {

    @Column(name = "iduser")
    private Long idUser;

    @Column(name = "idnotification")
    private Long idNotification;

}
