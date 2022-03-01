package br.com.harvest.onboardexperience.domain.dtos.forms;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PositionDTO {

    @NotNull
    @JsonProperty("xAxis")
    private BigDecimal xAxis;

    @NotNull
    @JsonProperty("yAxis")
    private BigDecimal yAxis;

    @JsonSetter("xAxis")
    public void setXAxis(@NonNull BigDecimal xAxis) {
        this.xAxis = xAxis.setScale(4, RoundingMode.HALF_UP);
    }

    @JsonSetter("yAxis")
    public void setYAxis(@NonNull BigDecimal yAxis) {
        this.yAxis = yAxis.setScale(4, RoundingMode.HALF_UP);
    }
}
