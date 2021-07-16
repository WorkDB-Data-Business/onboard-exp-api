package br.com.harvest.onboardexperience.domain.dto;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
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
	
	@NotNull
	private RoleEnum role;
	
	@NotEmpty
	private Set<PermissionDto> permissions;

}
