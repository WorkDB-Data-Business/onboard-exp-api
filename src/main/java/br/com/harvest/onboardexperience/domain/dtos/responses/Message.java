package br.com.harvest.onboardexperience.domain.dtos.responses;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Message {
	
	private String message;
	private List<MessageError> errors;
	private List<Comment> comments;
	
	public Message() {
		this.errors = new ArrayList<>();
		this.comments = new ArrayList<>();
	}
	
}
