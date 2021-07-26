package br.com.harvest.onboardexperience.domain.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinDto {
	
	private Long id;
	
	@JsonIgnore
	private String imagePath;
	
	@NotEmpty
	private String name;
	
	@JsonProperty(access = Access.READ_ONLY)
	private ClientDto client;
	
	@Builder.Default
	private Boolean isActive = true;

}
