package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScormCloudMetricsDTO {

    @JsonProperty("quantityScormRegistrationsCurrentlyActive")
    private BigInteger quantityScormRegistrationsCurrentlyActive;

    @JsonProperty("maxRegistrationsInThePlan")
    private BigInteger maxRegistrationsInThePlan;

    @JsonProperty("currentPlan")
    private String currentPlan;

    @JsonProperty("percentageUsageRegistrationPlan")
    private BigDecimal percentageUsageRegistrationPlan;

}
