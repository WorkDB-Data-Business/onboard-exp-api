package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ColaboratorTrailMetrics {

    @JsonProperty("trailId")
    private Long trailId;

    @JsonProperty("trailName")
    private String trailName;

    @JsonProperty("finalizationPercentage")
    private BigDecimal finalizationPercentage;

    @JsonProperty("lastStopedStage")
    private LastStopedStage lastStopedStage;

}
