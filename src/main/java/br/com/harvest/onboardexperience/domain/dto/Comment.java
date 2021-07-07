package br.com.harvest.onboardexperience.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
	
	private String message;
	
	private String messageKey;

}
