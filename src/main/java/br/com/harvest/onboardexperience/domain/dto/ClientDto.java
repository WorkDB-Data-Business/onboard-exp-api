package br.com.harvest.onboardexperience.domain.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.harvest.onboardexperience.domain.entities.User;
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
public class ClientDto {
	
	private Long id;
	
	@NotBlank
	@Size(min = ValidationUtils.CNPJ_SIZE, max = ValidationUtils.CNPJ_SIZE)
	@Pattern(regexp = RegexUtils.ONLY_NUMBERS)
	private String cnpj;
	
	@NotBlank
	@Size(min = ValidationUtils.MIN_SIZE_COMPANY_NAME, max = ValidationUtils.MAX_SIZE_COMPANY_NAME)
	private String name;
	
	@JsonIgnore
	@NotNull
	private List<User> users;
	
	private Boolean isActive;
	
	private Boolean isExpired;
	
	private Boolean isBlocked;
	
	private String tenant;
	
	private Boolean isMaster;
}
