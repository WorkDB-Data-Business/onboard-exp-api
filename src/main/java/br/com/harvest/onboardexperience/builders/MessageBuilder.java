package br.com.harvest.onboardexperience.builders;

import java.util.Arrays;
import java.util.List;

import br.com.harvest.onboardexperience.domain.dto.responses.Comment;
import br.com.harvest.onboardexperience.domain.dto.responses.Message;
import br.com.harvest.onboardexperience.domain.dto.responses.MessageError;
import br.com.harvest.onboardexperience.utils.BuilderUtils;

public class MessageBuilder {

	private Message message = new Message();

	public MessageBuilder() {
		this.message = new Message();
	}

	public MessageBuilder addMessage(final String message) {

		BuilderUtils.validateNullObject(message, "Message");
		this.message.setMessage(message);

		return this;
	}
	
	public MessageBuilder withError(final String message, final String cause) {
		
		BuilderUtils.validateNullObject(message, "Message");
		BuilderUtils.validateNullObject(cause, "Cause");
		MessageError error = MessageError.builder().message(message).cause(cause).build();
		
		this.message.getErrors().add(error);
		return this;
	}
	

	public MessageBuilder withErrors(final List<MessageError> errors) {

		BuilderUtils.validateNullListObject(errors, "Errors");

		this.message.setErrors(errors);
		return this;
	}
	
	public MessageBuilder withErrors(final MessageError... errors) {

		BuilderUtils.validateNullListObject(errors, "Errors");
		
		for(MessageError errorParam: errors) {
			MessageError error = MessageError.builder()
					.message(errorParam.getMessage())
					.cause(errorParam.getCause())
					.build();
			this.message.getErrors().add(error);
		}

		return this;
	}
	
	public MessageBuilder withComments(final List<Comment> comments) {
		
		BuilderUtils.validateNullListObject(comments, "Comments");

		this.message.getComments().addAll(comments);
		
		return this;
	}
	
	public MessageBuilder withComments(final Comment... comments) {

		BuilderUtils.validateNullListObject(comments, "Comments");
		
		this.message.getComments().addAll(Arrays.asList(comments));	

		return this;
	}
	
	public MessageBuilder withComments(final String... comments) {

		BuilderUtils.validateNullListObject(comments, "Comments");
		
		for (String commentParam : comments) {
			Comment comment = Comment.builder().message(commentParam).build();
			this.message.getComments().add(comment);
		}

		return this;
	}

	public Message build() {
		return this.message;
	}
	
}
