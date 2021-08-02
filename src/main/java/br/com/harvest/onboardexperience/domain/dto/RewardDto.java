package br.com.harvest.onboardexperience.domain.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardDto {
	
	private Long id;
	
	@NotBlank
	private String imagePath;
	
	@NotBlank
	private String name;
	
	@DecimalMin(value = ValidationUtils.MIN_PRICE, inclusive = false)
	@Digits(fraction = ValidationUtils.MAX_PRICE_FRACTION, integer = ValidationUtils.MAX_PRICE)
	private BigDecimal price;
	
	@NotNull
	private ClientDto client;
	
	@Builder.Default
	private Boolean isActive = true;

}
