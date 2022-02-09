package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Data
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idtrail")
    private Trail trail;

    @Column(name = "is_pre_requisite")
    private Boolean isPreRequisite;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "x_axis"),
            @JoinColumn(name = "y_axis")
    })
    private Position position;

    @OneToMany(mappedBy = "stage")
    private List<StageUser> stageUsers;

    @OneToMany(mappedBy = "stage")
    private List<ScormMediaStage> scorms;

    @OneToMany(mappedBy = "stage")
    private List<HarvestFileMediaStage> files;

    @OneToMany(mappedBy = "stage")
    private List<LinkMediaStage> links;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stage stage = (Stage) o;
        return Objects.equals(id, stage.id) && Objects.equals(name, stage.name) && Objects.equals(description, stage.description) && Objects.equals(amountCoins, stage.amountCoins) && Objects.equals(minimumScore, stage.minimumScore) && Objects.equals(trail, stage.trail) && Objects.equals(isPreRequisite, stage.isPreRequisite) && Objects.equals(position, stage.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, amountCoins, minimumScore, trail, isPreRequisite, position);
    }
}
