package br.com.harvest.onboardexperience.domain.dto;


import javax.validation.constraints.NotNull;

import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
	
	private Long id;
	
	@NotNull
	private PermissionEnum permission;

}
