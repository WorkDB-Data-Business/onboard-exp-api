package br.com.harvest.onboardexperience.domain.dtos.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PositionForm {

    @Builder
    @JsonCreator
    public PositionForm(BigInteger xAxis, BigInteger yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @JsonProperty("xAxis")
    private BigInteger xAxis;

    @JsonProperty("yAxis")
    private BigInteger yAxis;

}
