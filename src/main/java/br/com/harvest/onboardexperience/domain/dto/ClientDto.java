package br.com.harvest.onboardexperience.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
public class ClientDto implements Serializable {
	
	private static final long serialVersionUID = 5480431954426852444L;

	private Long id;
	
	@NotBlank
	@Size(min = ValidationUtils.CNPJ_SIZE, max = ValidationUtils.CNPJ_SIZE)
	@Pattern(regexp = RegexUtils.ONLY_NUMBERS)
	private String cnpj;
	
	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_COMPANY_NAME, max = ValidationUtils.MAX_SIZE_COMPANY_NAME)
	private String name;
	
	@Builder.Default
	private Boolean isActive = true;
	
	@Builder.Default
	private Boolean isExpired = false;
	
	@Builder.Default
	private Boolean isBlocked = false;
	
	@NotBlank
	private String tenant;
	
	@Builder.Default
	private Boolean isMaster = false;
	
	@NotBlank
	@Pattern(regexp = RegexUtils.EMAIL_VALIDATION)
	private String email;
}
