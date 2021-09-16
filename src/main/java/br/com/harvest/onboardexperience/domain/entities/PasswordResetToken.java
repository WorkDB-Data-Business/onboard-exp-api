package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tbpassword_reset_token", schema="public")
public class PasswordResetToken extends BaseEntity {

    @Id
    @Column(name = "idpassword_reset_token")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(name = "is_expired")
    private Boolean isExpired;

}
