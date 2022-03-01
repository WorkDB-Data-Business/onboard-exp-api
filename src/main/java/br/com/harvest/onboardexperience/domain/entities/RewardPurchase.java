package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbreward_purchase")
@EntityListeners(AuditingEntityListener.class)
public class RewardPurchase extends BaseEntity {

    private static final long serialVersionUID = 4238287226031163965L;

    @Id
    @Column(name = "idreward_purchase")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idreward")
    private Reward reward;

    @CreatedDate
    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;

    @Builder.Default
    @Column(name = "have_consumed")
    private Boolean haveConsumed = false;

}
