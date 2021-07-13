package br.com.harvest.onboardexperience.domain.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.harvest.onboardexperience.domain.dto.responses.Comment;
import br.com.harvest.onboardexperience.domain.dto.responses.MessageError;
import br.com.harvest.onboardexperience.domain.exceptions.FactoryException;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.FactoryExceptionEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageErrorBuilder {

	private List<MessageError> errors;
	private MessageError error;

	public MessageErrorBuilder() {
		this.errors = new ArrayList<>();
	}

	public MessageErrorBuilder buildError(final MessageError error) {
		
		if(Objects.isNull(error)) {
			log.error(FactoryExceptionEnum.ERROR_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.ERROR_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.ERROR_CANNOT_BE_NULL);
		}
		
		this.errors.add(error);
		
		return this;
	}

	public MessageErrorBuilder buildError(final String message, final String cause, String messageKey, String causeKey) {
		
		if(Objects.isNull(message)) {
			log.error(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL);
		}
		
		if(Objects.isNull(cause)) {
			log.error(FactoryExceptionEnum.CAUSE_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.CAUSE_CANNOT_BE_NULL);
		}
		
		if(Objects.isNull(messageKey)) {
			log.error(FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL);
		}
		
		if(Objects.isNull(causeKey)) {
			log.error(FactoryExceptionEnum.CAUSE_KEY_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.CAUSE_KEY_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.CAUSE_KEY_CANNOT_BE_NULL);
		}
		
		this.error = MessageError.builder()
						  .message(message)
						  .messageKey(messageKey)
						  .cause(cause)
						  .causeKey(causeKey)
						  .build(); 
		
		return this;
	}
	
	public MessageErrorBuilder withComments(final List<Comment> comments) {
		
		if(Objects.isNull(comments)) {
			log.error(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL);
		}
		
		if(Objects.isNull(this.error)) {
			log.error(FactoryExceptionEnum.CURRENT_ERROR_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.CURRENT_ERROR_NULL);
		}
		
		this.error.setComments(comments);
		
		return this;
	}
	
	public MessageErrorBuilder addError() {
		
		if(Objects.isNull(this.error)) {
			log.error(FactoryExceptionEnum.CURRENT_ERROR_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.CURRENT_ERROR_NULL);
		} 
		
		this.errors.add(error);
		this.error = null;
		return this;
	}

	public List<MessageError> build() {
		return this.errors;
	}

}
