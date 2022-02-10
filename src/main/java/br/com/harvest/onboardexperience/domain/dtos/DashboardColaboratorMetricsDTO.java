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

    @JsonProperty("userTrailMetrics")
    private List<UserTrailMetrics> userTrailMetrics;

}
