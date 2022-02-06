package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.HarvestFileMediaUserId;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(HarvestFileMediaUserId.class)
@Entity(name="tbstage_file_user")
public class HarvestFileMediaUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "idfile")
    private HarvestFile harvestFile;

    @Id
    @ManyToOne
    @JoinColumn(name = "idstage")
    private Stage stage;

    @Id
    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
