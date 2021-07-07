package br.com.harvest.onboardexperience.domain.factory;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

import br.com.harvest.onboardexperience.domain.dto.Comment;
import br.com.harvest.onboardexperience.domain.dto.MessageError;
import br.com.harvest.onboardexperience.domain.dto.Message;
import br.com.harvest.onboardexperience.domain.exception.FactoryException;
import br.com.harvest.onboardexperience.domain.exception.enumerators.FactoryExceptionEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageFactory {
	
	private Message message;
	
	public MessageFactory() {
		this.message = new Message();
	}
	
	public MessageFactory buildMessage(final String message) {
		
		if(Objects.isNull(message)) {
			log.error(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL);
		}
		
		this.message.setMessage(message);
		
		return this;
	}
	
	public MessageFactory setMessageKey(final String messageKey) {
		
		if(Objects.isNull(message)) {
			log.error(FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL);
		}
		
		this.message.setMessageKey(messageKey);
		
		return this;
	}
	
	public MessageFactory withErrors(final List<MessageError> errors) {
		
		if(ObjectUtils.isEmpty(errors)) {
			log.error(FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL);
		}
		
		this.message.setErrors(errors);
		return this;
	}
	
	public MessageFactory withComments(final List<Comment> comments) {
		
		if(ObjectUtils.isEmpty(comments)) {
			log.error(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL);
		}
		
		this.message.setComments(comments);

		return this;
	}
	
	public Message build() {
		return this.message;
	}

}
