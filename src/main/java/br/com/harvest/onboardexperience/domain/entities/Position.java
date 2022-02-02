package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PositionId.class)
@Entity(name = "tbposition")
@EqualsAndHashCode
public class Position {

    @Id
    @Column(name = "xAxis")
    private BigInteger xAxis;

    @Id
    @Column(name = "yAxis")
    private BigInteger yAxis;

}
