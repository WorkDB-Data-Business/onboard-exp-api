package br.com.harvest.onboardexperience.domain.dtos;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoleDto implements Serializable {
	
	private static final long serialVersionUID = -894423581234757315L;

	private Long id;
	
	@NotNull
	private RoleEnum role;
	
	@NotEmpty
	private Set<PermissionDto> permissions;

}
