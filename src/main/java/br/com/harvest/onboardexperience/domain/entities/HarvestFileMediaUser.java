package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.HarvestFileMediaUserId;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(HarvestFileMediaUserId.class)
@Entity(name="tbstage_file_user")
public class HarvestFileMediaUser {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "idfile"),
            @JoinColumn(name = "idstage")
    })
    private HarvestFileMediaStage harvestFileMedia;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iduser")
    private User user;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
