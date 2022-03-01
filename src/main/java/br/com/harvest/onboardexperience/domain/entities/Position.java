package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.PositionId;
import br.com.harvest.onboardexperience.domain.entities.listeners.PositionListener;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @Column(name = "x_axis")
    private BigDecimal xAxis;

    @Id
    @Column(name = "y_axis")
    private BigDecimal yAxis;

}
