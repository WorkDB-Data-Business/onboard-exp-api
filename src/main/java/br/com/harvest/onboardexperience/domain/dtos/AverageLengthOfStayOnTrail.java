package br.com.harvest.onboardexperience.domain.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AverageLengthOfStayOnTrail {

    private Long days;

    private Integer hours;

    private Integer minutes;

}
