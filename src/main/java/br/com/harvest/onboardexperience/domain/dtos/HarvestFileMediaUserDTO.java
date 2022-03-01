package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HarvestFileMediaUserDTO {

    @JsonProperty("harvestFileMedia")
    private HarvestFileMediaStageDTO harvestFileMedia;

    @JsonProperty("isCompleted")
    private Boolean isCompleted;

    @JsonProperty("completedAt")
    private LocalDateTime completedAt;

}
