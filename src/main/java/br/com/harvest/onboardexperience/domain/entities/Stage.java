package br.com.harvest.onboardexperience.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbstage", schema="public")
public class Stage extends BaseEntityAudit {

    @Id
    @Column(name = "idstage")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "amount_coins")
    private BigInteger amountCoins;

    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "idcoin")
    private Coin coin;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "is_prerequisite")
    private Boolean isPrerequisite;

    @Column(name = "is_muted")
    private Boolean isMuted;



}
