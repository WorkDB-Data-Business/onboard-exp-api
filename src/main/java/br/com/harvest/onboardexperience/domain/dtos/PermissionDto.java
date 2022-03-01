package br.com.harvest.onboardexperience.domain.dtos;


import java.io.Serializable;

import javax.validation.constraints.NotNull;

import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionDto implements Serializable {
	
	private static final long serialVersionUID = -4606418091317413664L;

	private Long id;
	
	@NotNull
	private PermissionEnum permission;

}
