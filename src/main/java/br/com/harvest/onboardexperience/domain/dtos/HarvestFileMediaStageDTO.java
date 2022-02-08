package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.infra.storage.dtos.FileIdDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HarvestFileMediaStageDTO {

    @JsonProperty("harvestFile")
    private FileIdDTO harvestFile;

}
