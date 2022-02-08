package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.StageUserId;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StageUserId.class)
@Entity(name = "tbstage_user")
public class StageUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "idstage")
    private Stage stage;

    @Column(name = "score")
    private BigDecimal score;

    @Builder.Default
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "idstage", insertable = false, updatable = false),
            @JoinColumn(name = "iduser", insertable = false, updatable = false)
    })
    private List<ScormMediaUser> scorms = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "idstage", insertable = false, updatable = false),
            @JoinColumn(name = "iduser", insertable = false, updatable = false)
    })
    private List<HarvestFileMediaUser> harvestFiles = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "idstage", insertable = false, updatable = false),
            @JoinColumn(name = "iduser", insertable = false, updatable = false)
    })
    private List<LinkMediaUser> links = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageUser stageUser = (StageUser) o;
        return Objects.equals(user, stageUser.user) && Objects.equals(stage, stageUser.stage) && Objects.equals(score, stageUser.score) && Objects.equals(isCompleted, stageUser.isCompleted) && Objects.equals(completedAt, stageUser.completedAt) && Objects.equals(startedAt, stageUser.startedAt) && Objects.equals(scorms, stageUser.scorms) && Objects.equals(harvestFiles, stageUser.harvestFiles) && Objects.equals(links, stageUser.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, stage, score, isCompleted, completedAt, startedAt, scorms, harvestFiles, links);
    }
}
