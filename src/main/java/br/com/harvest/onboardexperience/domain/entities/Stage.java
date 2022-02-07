package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbstage")
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

    @Column(name = "minimum_score")
    private BigDecimal minimumScore;

    @ManyToOne
    @JoinColumn(name = "idtrail")
    private Trail trail;

    @Column(name = "is_pre_requisite")
    private Boolean isPreRequisite;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "x_axis"),
            @JoinColumn(name = "y_axis")
    })
    private Position position;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<ScormMediaStage> scorms;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<HarvestFileMediaStage> files;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<LinkMediaStage> links;

}
