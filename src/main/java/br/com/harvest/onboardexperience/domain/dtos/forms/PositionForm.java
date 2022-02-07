package br.com.harvest.onboardexperience.domain.dtos.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PositionForm {

    @Builder
    @JsonCreator
    public PositionForm(BigDecimal xAxis, BigDecimal yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @JsonProperty("xAxis")
    private BigDecimal xAxis;

    @JsonProperty("yAxis")
    private BigDecimal yAxis;

}
