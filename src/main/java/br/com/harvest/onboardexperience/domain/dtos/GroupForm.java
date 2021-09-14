package br.com.harvest.onboardexperience.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupForm {

    private Long id;

    private String name;

    private Boolean isActive;

    private List<Long> users;

    private List<Long> companyRoles;

}
