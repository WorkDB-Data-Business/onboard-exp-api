package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {

    private Long id;

    @NotBlank
    private String nameEvent;

    @NotBlank
    @Size(min = ValidationUtils.MIN_SIZE_DESCRIPTION)
    private String descriptionEvent;

    @Builder.Default
    private Boolean isActive = true;

    private Boolean typeEvent;

}
