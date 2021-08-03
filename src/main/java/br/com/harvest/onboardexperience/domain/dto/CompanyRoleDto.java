package br.com.harvest.onboardexperience.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRoleDto implements Serializable {
	
	private static final long serialVersionUID = 5968911277203889949L;

	private Long id;
	
	@NotBlank
	@Min(ValidationUtils.MIN_SIZE_ROLE)
	private String name;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotNull
	private ClientDto client;
	
	@Builder.Default
	private Boolean isActive = true;
}
