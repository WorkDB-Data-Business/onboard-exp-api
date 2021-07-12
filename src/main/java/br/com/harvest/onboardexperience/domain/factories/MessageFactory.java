package br.com.harvest.onboardexperience.domain.factories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.harvest.onboardexperience.domain.builders.MessageBuilder;
import br.com.harvest.onboardexperience.domain.dto.responses.Comment;
import br.com.harvest.onboardexperience.domain.dto.responses.Message;
import br.com.harvest.onboardexperience.domain.dto.responses.MessageError;
import br.com.harvest.onboardexperience.domain.dto.responses.SimpleMessage;
import br.com.harvest.onboardexperience.domain.dto.responses.SimpleMessageWithComment;
import br.com.harvest.onboardexperience.domain.enumerators.interfaces.EnumInterface;
import br.com.harvest.onboardexperience.domain.mappers.MessageMapper;

@Component
public class MessageFactory {
	
	@Autowired
	private MessageMapper messageMapper;
	
	private MessageBuilder messageBuilder;
	
	public MessageFactory() {
		this.messageBuilder = new MessageBuilder();
	}
	
	public SimpleMessage createSimpleMessage(final EnumInterface message) {
		return messageMapper.toSimpleMessage(this.messageBuilder.buildMessage(message).build());
	}
	
	public SimpleMessageWithComment createMessageWithComment(final EnumInterface message, final List<Comment> comments) {
		return messageMapper.toSimpleMessageWithComment(this.messageBuilder.buildMessage(message).withComments(comments)
				.build());
	}
	
	public SimpleMessageWithComment createMessageWithComment(final EnumInterface message, final EnumInterface... comments) {
		return messageMapper.toSimpleMessageWithComment(this.messageBuilder.buildMessage(message).withComments(comments)
				.build());
	}
	
	public Message createMessage(final EnumInterface message, final List<Comment> comments, final List<MessageError> errors) {
		return this.messageBuilder.buildMessage(message).withComments(comments).withErrors(errors).build();
	}
	
	public Message createMessage(final EnumInterface message, final List<Comment> comments, final MessageError... errors) {
		return this.messageBuilder.buildMessage(message).withComments(comments).withErrors(errors).build();
	}
	
	public Message createMessage(final EnumInterface message, final List<MessageError> errors, final EnumInterface...comments ) {
		return this.messageBuilder.buildMessage(message).withComments(comments).withErrors(errors).build();
	}	
	

}
