package br.com.harvest.onboardexperience.domain.dtos.forms;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRoleForm {

    @NotBlank
    @Size(min = ValidationUtils.MIN_SIZE_ROLE)
    private String name;

    @Builder.Default
    private Boolean isActive = true;

}
