package br.com.harvest.onboardexperience.domain.dto;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRoleDto {
	
	private Long id;
	
	@NotBlank
	@Min(ValidationUtils.MIN_SIZE_ROLE)
	private String name;
	
	@NotNull
	private List<UserDto> users;
	
}
