package br.com.harvest.onboardexperience.domain.dto.responses;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleMessageWithComment {

	private String message;
	private String messageKey;
	private List<Comment> comments; 
	
}
