package br.com.harvest.onboardexperience.domain.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.harvest.onboardexperience.utils.RegexUtils;
import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private Long id;
	
	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_NAME , max = ValidationUtils.MAX_SIZE_NAME)
	private String firstName;
	
	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_NAME , max = ValidationUtils.MAX_SIZE_NAME)
	private String lastName;
	
	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_USERNAME , max = ValidationUtils.MAX_SIZE_USERNAME)
	private String username;
	
//	@Pattern(regexp = RegexUtils.PASSWORD_VALIDATION) TODO: Commented line to facilitate testing, uncomment when it's necessary
	@NotBlank
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@NotBlank
	@Pattern(regexp = RegexUtils.EMAIL_VALIDATION)
	private String email;
	
	private String cpf;
	
	private CompanyRoleDto companyRole;
	
	private Boolean isActive;
	
	private Boolean isBlocked;
	
	private Boolean isExpired;
	
	@NotEmpty
	private Set<RoleDto> roles;
	
}
