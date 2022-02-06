package br.com.harvest.onboardexperience.domain.dtos.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PositionDTO {

    @Builder
    @JsonCreator
    public PositionDTO(BigDecimal xAxis, BigDecimal yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @NotNull
    @JsonProperty("xAxis")
    private BigDecimal xAxis;

    @NotNull
    @JsonProperty("yAxis")
    private BigDecimal yAxis;

}
