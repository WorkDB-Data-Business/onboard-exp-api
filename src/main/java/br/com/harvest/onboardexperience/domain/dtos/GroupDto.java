package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.enumerators.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    private Long id;

    private String name;

    private Boolean isActive;

    private ClientDto client;

    private List<UserDto> users;

    private List<CompanyRoleDto> companyRoles;

}
