package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DashboardAdminMetricsDTO {

    @JsonProperty("trailMetrics")
    private List<TrailMetrics> trailMetrics;

    @JsonProperty("harvestLibraryRanking")
    private List<HarvestLibraryRanking> harvestLibraryRanking;

}
