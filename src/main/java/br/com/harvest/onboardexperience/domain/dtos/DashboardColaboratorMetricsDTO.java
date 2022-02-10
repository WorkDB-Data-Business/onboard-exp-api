package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DashboardColaboratorMetricsDTO {

    @JsonProperty("colaboratorTrailMetrics")
    private List<ColaboratorTrailMetrics> colaboratorTrailMetrics;

    @JsonProperty("totalCoinColaborator")
    private TotalCoinColaborator totalCoinColaborator;
}
