package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.infra.storage.dtos.LinkIdDTO;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkSimpleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LinkMediaStageDTO {

    @JsonProperty("link")
    private LinkIdDTO link;

}
