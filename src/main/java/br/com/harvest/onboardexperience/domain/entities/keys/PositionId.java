package br.com.harvest.onboardexperience.domain.entities.keys;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionId implements Serializable {

    private BigDecimal xAxis;

    private BigDecimal yAxis;

}
