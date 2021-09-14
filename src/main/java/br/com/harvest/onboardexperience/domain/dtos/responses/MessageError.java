package br.com.harvest.onboardexperience.domain.dtos.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MessageError {
	
	private String message; 
	private String cause;
	private List<Comment> comments;
	
	public MessageError() {
		this.comments = new ArrayList<>();
	}
	
}
