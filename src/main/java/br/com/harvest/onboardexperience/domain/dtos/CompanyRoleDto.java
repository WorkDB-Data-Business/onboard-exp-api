package br.com.harvest.onboardexperience.domain.dtos;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CompanyRoleDto implements Serializable {
	
	private static final long serialVersionUID = 5968911277203889949L;

	private Long id;
	
	@NotBlank
	@Min(ValidationUtils.MIN_SIZE_ROLE)
	private String name;

	@JsonIgnore
	private ClientDto client;
	
	@Builder.Default
	private Boolean isActive = true;
}
