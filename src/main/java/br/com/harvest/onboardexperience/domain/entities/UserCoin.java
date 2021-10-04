package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Builder
@Setter
@Entity(name = "tbuser_coin")
@NoArgsConstructor
@AllArgsConstructor
public class UserCoin {

    @EmbeddedId
    private UserCoinKey id;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne
    @MapsId("idCoin")
    @JoinColumn(name = "idcoin")
    private Coin coin;

    @Builder.Default
    @Column(name = "amount")
    private BigInteger amount = new BigInteger("0");

}
