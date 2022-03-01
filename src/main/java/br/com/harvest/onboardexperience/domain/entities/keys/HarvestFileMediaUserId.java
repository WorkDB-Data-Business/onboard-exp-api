package br.com.harvest.onboardexperience.domain.entities.keys;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HarvestFileMediaUserId implements Serializable {

    private HarvestFileMediaStageId harvestFileMedia;

    private Long user;

}
