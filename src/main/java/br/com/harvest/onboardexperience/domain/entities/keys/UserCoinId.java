package br.com.harvest.onboardexperience.domain.entities.keys;

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
public class UserCoinId implements Serializable {

    @Column(name = "iduser")
    private Long idUser;

    @Column(name = "idcoin")
    private Long idCoin;

}
