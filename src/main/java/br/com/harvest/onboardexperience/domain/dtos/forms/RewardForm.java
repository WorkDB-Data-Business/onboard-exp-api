package br.com.harvest.onboardexperience.domain.dtos.forms;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RewardForm {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = ValidationUtils.MIN_SIZE_DESCRIPTION)
    private String description;

    @NotNull
    @DecimalMin(value = ValidationUtils.MIN_PRICE, inclusive = false)
    @Digits(fraction = ValidationUtils.MAX_PRICE_FRACTION, integer = ValidationUtils.MAX_PRICE)
    private BigDecimal price;

    @Builder.Default
    private Boolean isActive = true;

    @NotNull
    private Long coinId;

}
