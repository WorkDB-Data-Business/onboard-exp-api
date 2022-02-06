package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.HarvestFileMediaStageId;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(HarvestFileMediaStageId.class)
@Entity(name="tbstage_file")
public class HarvestFileMediaStage {

    @Id
    @ManyToOne
    @JoinColumn(name = "idfile")
    private HarvestFile harvestFile;

    @Id
    @ManyToOne
    @JoinColumn(name = "idstage")
    private Stage stage;

}
