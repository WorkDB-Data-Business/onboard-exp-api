package br.com.harvest.onboardexperience.domain.dtos.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupForm {

    private Long id;

    @NotBlank
    private String name;

    @Builder.Default
    private Boolean isActive = true;

    private List<Long> users;

    private List<Long> companyRoles;

}
