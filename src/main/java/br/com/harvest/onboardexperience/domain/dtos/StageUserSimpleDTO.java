package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StageUserSimpleDTO {

    @JsonProperty("score")
    private BigDecimal score;

    @JsonProperty("isCompleted")
    private Boolean isCompleted;

}
