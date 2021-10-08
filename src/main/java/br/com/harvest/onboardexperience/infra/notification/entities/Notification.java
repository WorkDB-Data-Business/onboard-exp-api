package br.com.harvest.onboardexperience.infra.notification.entities;

import br.com.harvest.onboardexperience.domain.entities.BaseEntity;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbnotification")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {

    private static final long serialVersionUID = -413978856084833669L;

    @Id
    @Column(name = "idnotification")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "was_visualized")
    private Boolean wasVisualized;

    @CreatedDate
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @JoinColumn(name = "author")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "tbnotification_user",
            joinColumns = @JoinColumn(name = "idnotification_user"),
            inverseJoinColumns = @JoinColumn(name = "iduser"))
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "tbnotification_client",
            joinColumns = @JoinColumn(name = "idnotification_client"),
            inverseJoinColumns = @JoinColumn(name = "idclient"))
    private List<Client> clients;


}
