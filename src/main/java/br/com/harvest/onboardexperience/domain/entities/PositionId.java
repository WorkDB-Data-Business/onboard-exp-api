package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

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
