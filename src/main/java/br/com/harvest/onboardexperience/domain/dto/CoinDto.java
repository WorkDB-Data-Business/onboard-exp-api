package br.com.harvest.onboardexperience.domain.dto;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

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
	
	@NotEmpty
	private String imagePath;
	
	@NotEmpty
	private String name;
	
	@NotNull
	private ClientDto client;
	
	@Builder.Default
	private Boolean isActive = true;

}
