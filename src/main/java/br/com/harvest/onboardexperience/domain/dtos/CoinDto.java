package br.com.harvest.onboardexperience.domain.dtos;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CoinDto {

	private Long id;

	@JsonProperty(access = Access.READ_ONLY)
	private String imagePath;

	@NotEmpty
	private String name;

	@JsonIgnore
	private ClientDto client;

	@Builder.Default
	private Boolean isActive = true;

}
