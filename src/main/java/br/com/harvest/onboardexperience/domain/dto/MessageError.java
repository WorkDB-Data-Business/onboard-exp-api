package br.com.harvest.onboardexperience.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MessageError {
	
	public MessageError() {
		this.comments = new ArrayList<>();
	}
	
	private String message;
	
	private String messageKey;
	
	private String cause;
	
	private String causeKey;
	
	private List<Comment> comments;

}
