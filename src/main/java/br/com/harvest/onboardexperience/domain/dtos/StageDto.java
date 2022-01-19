package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StageDto {
	
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_DESCRIPTION)
	private String description;

	private Long amountCoins;

	@Builder.Default
	private Boolean isActive = true;

	@Builder.Default
	private Boolean isAvailable = false;

	@Builder.Default
	private Boolean isPrerequisite = false;

	@Builder.Default
	private Boolean isMuted = false;

	private Long coinId;

}
