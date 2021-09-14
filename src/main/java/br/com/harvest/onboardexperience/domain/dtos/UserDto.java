package br.com.harvest.onboardexperience.domain.dtos;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class UserDto implements Serializable {
	
	private static final long serialVersionUID = -9144485778635865671L;

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
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@NotBlank
	@Pattern(regexp = RegexUtils.EMAIL_VALIDATION)
	private String email;
	
	@Size(min = ValidationUtils.CPF_SIZE, max = ValidationUtils.CPF_SIZE)
	@Pattern(regexp = RegexUtils.ONLY_NUMBERS)
	private String cpf;
	
	@NotNull
	private CompanyRoleDto companyRole;
	
	@Builder.Default
	private Boolean isActive = true;
	
	@Builder.Default
	private Boolean isBlocked = false;
	
	@Builder.Default
	private Boolean isExpired = false;
	
	@Builder.Default
	private Boolean isFirstLogin = true;
	
	@NotEmpty
	private Set<RoleDto> roles;
	
	@JsonProperty(access = Access.READ_ONLY)
	private ClientDto client;
	
	@Builder.Default
	private Boolean isClient = false;
	
}
