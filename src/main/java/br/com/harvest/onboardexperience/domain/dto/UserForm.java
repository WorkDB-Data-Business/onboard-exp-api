package br.com.harvest.onboardexperience.domain.dto;

import br.com.harvest.onboardexperience.utils.RegexUtils;
import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserForm implements Serializable {
	
	private static final long serialVersionUID = -9062421413300029902L;

	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_NAME , max = ValidationUtils.MAX_SIZE_NAME)
	private String firstName;
	
	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_NAME , max = ValidationUtils.MAX_SIZE_NAME)
	private String lastName;
	
	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_USERNAME , max = ValidationUtils.MAX_SIZE_USERNAME)
	private String username;

	@NotBlank
	@Pattern(regexp = RegexUtils.EMAIL_VALIDATION)
	private String email;
	
	@Size(min = ValidationUtils.CPF_SIZE, max = ValidationUtils.CPF_SIZE)
	@Pattern(regexp = RegexUtils.ONLY_NUMBERS)
	private String cpf;
	
	@NotNull
	private long companyRoleId;
	
	@Builder.Default
	private Boolean isActive = true;
	
	@Builder.Default
	private Boolean isBlocked = false;

	@Builder.Default
	private Boolean isAdmin = false;

	@Builder.Default
	private Boolean isCol = false;

	@Builder.Default
	private Boolean isMaster = false;
}
