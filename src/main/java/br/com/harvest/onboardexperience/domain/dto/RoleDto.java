package br.com.harvest.onboardexperience.domain.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
	
	private Long id;
	
	@Size(min = ValidationUtils.MIN_SIZE_ROLE, max = ValidationUtils.MAX_SIZE_ROLE)
	private String name;
	
	@NotEmpty
	private Set<PermissionDto> permissions;

}
