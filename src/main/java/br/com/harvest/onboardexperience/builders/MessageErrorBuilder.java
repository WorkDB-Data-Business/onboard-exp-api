package br.com.harvest.onboardexperience.builders;

import java.util.ArrayList;
import java.util.List;

import br.com.harvest.onboardexperience.domain.dto.responses.Comment;
import br.com.harvest.onboardexperience.domain.dto.responses.MessageError;
import br.com.harvest.onboardexperience.utils.BuilderUtils;

public class MessageErrorBuilder {

	private List<MessageError> errors = new ArrayList<>();
	private MessageError error;

	public MessageErrorBuilder() {
		this.errors = new ArrayList<>();
	}

	public MessageErrorBuilder addError(final MessageError error) {
		
		BuilderUtils.validateNullObject(error, "Error");
		
		this.errors.add(error);
		
		return this;
	}

	public MessageErrorBuilder addError(final String message, final String cause) {
		
		BuilderUtils.validateNullObject(message, "Message");
		BuilderUtils.validateNullObject(cause, "Cause");
		
		this.error = MessageError.builder()
						  .message(message)
						  .cause(cause)
						  .build(); 
		
		return this;
	}
	
	public MessageErrorBuilder withComments(final List<Comment> comments) {
		
		BuilderUtils.validateNullListObject(comments, "Comments");
		
		BuilderUtils.validateNullListObject(this.errors, "Current errors");
		
		this.error.setComments(comments);
		
		return this;
	}
	
	public MessageErrorBuilder finalizeError() {
		
		BuilderUtils.validateNullObject(this.error, "Current error");
		
		this.errors.add(error);
		this.error = null;
		return this;
	}

	public List<MessageError> build() {
		return this.errors;
	}

}
