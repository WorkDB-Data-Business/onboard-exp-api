package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.entities.HarvestFileMediaStage;
import br.com.harvest.onboardexperience.domain.entities.LinkMediaStage;
import br.com.harvest.onboardexperience.domain.entities.ScormMediaStage;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StageDTO {

	private Long id;

	private String name;

	private String description;

	private BigInteger amountCoins;

	private BigDecimal minimumScore;

	private Boolean isPreRequisite;

	private PositionDTO position;

	private List<MediaExecution> mediaExecutions;

}
