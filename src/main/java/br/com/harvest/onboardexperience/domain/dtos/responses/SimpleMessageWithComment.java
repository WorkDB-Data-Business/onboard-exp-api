package br.com.harvest.onboardexperience.domain.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessageWithComment {

	private String message;
	private List<Comment> comments; 
	
}
