package br.com.harvest.onboardexperience.domain.dtos.forms;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;

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
    @Min(ValidationUtils.MIN_PRICE)
    private BigInteger price;

    @Builder.Default
    private Boolean isActive = true;

    @NotNull
    private Long coinId;

}
