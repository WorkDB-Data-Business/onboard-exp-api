package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.entities.HarvestFileMediaStage;
import br.com.harvest.onboardexperience.domain.entities.LinkMediaStage;
import br.com.harvest.onboardexperience.domain.entities.ScormMediaStage;

import com.fasterxml.jackson.annotation.JsonProperty;
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

	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("amountCoins")
	private BigInteger amountCoins;

	@JsonProperty("minimumScore")
	private BigDecimal minimumScore;

	@JsonProperty("isPreRequisite")
	private Boolean isPreRequisite;

	@JsonProperty("position")
	private PositionDTO position;

	@JsonProperty("mediaExecutions")
	private List<MediaExecution> mediaExecutions;

}
