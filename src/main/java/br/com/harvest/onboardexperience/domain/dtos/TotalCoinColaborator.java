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
public class TotalCoinColaborator {

    @JsonProperty("totalColaboratorCoins")
    private BigInteger totalColaboratorCoins;

    @JsonProperty("totalPossibleCoinsFromTrails")
    private BigInteger totalPossibleCoinsFromTrails;

    @JsonProperty("percentageFromTotal")
    private BigDecimal percentageFromTotal;

}
