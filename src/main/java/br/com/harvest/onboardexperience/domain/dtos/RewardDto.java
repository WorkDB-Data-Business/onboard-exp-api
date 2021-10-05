package br.com.harvest.onboardexperience.domain.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RewardDto {
	
	private Long id;

	@JsonProperty(access = Access.READ_ONLY)
	private String imagePath;
	
	@NotBlank
	private String name;

	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_DESCRIPTION)
	private String description;

	@NotNull
	@DecimalMin(value = ValidationUtils.MIN_PRICE, inclusive = false)
	@Digits(fraction = ValidationUtils.MAX_PRICE_FRACTION, integer = ValidationUtils.MAX_PRICE)
	private BigDecimal price;
	
	@JsonIgnore
	private ClientDto client;
	
	@Builder.Default
	private Boolean isActive = true;

	private CoinDto coin;

}
