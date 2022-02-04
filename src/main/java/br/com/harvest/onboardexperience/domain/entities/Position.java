package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.listeners.PositionListener;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PositionId.class)
@Entity(name = "tbposition")
@EqualsAndHashCode
@EntityListeners(PositionListener.class)
public class Position {

    @Id
    @Column(name = "xAxis")
    private BigDecimal xAxis;

    @Id
    @Column(name = "yAxis")
    private BigDecimal yAxis;

}
