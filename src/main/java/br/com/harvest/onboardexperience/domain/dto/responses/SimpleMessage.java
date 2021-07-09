package br.com.harvest.onboardexperience.domain.dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleMessage {
	
	private String message;
	private String messageKey;
	
}
