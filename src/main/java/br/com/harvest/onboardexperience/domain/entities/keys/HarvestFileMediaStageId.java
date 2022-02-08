package br.com.harvest.onboardexperience.domain.entities.keys;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HarvestFileMediaStageId implements Serializable {

    private Long harvestFile;

    private Long stage;

}
