package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrailMetrics {

    @JsonProperty("quantityActiveUsers")
    private BigInteger quantityActiveUsers;

    @JsonProperty("id")
    private Long trailId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("userMetrics")
    private List<UserMetrics> userMetrics;

}
