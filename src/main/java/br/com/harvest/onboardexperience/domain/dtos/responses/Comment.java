package br.com.harvest.onboardexperience.domain.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
	
	private String message;
	
}
