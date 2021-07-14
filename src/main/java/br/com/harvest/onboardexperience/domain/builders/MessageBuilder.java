//package br.com.harvest.onboardexperience.domain.builders;
//
//import java.util.List;
//import java.util.Objects;
//
//import org.apache.commons.lang3.ObjectUtils;
//
//import br.com.harvest.onboardexperience.domain.dto.responses.Comment;
//import br.com.harvest.onboardexperience.domain.dto.responses.Message;
//import br.com.harvest.onboardexperience.domain.dto.responses.MessageError;
//import br.com.harvest.onboardexperience.domain.enumerators.interfaces.EnumInterface;
//import br.com.harvest.onboardexperience.domain.exceptions.FactoryException;
//import br.com.harvest.onboardexperience.domain.exceptions.enumerators.FactoryExceptionEnum;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class MessageBuilder {
//
//	private Message message;
//
//	public MessageBuilder() {
//		this.message = new Message();
//	}
//
//	public MessageBuilder buildMessage(final EnumInterface message) {
//
//		if(Objects.isNull(message)) {
//			log.error(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
//			throw new FactoryException(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL);
//		}
//
//		this.message.setMessage(message.getValue());
//		this.message.setMessageKey(message.getValueKey());
//
//		return this;
//	}
//	
//
//	public MessageBuilder withErrors(final List<MessageError> errors) {
//
//		if(ObjectUtils.isEmpty(errors)) {
//			log.error(FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL.getCause());
//			throw new FactoryException(FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL);
//		}
//
//		this.message.setErrors(errors);
//		return this;
//	}
//	
//	public MessageBuilder withErrors(final MessageError... errors) {
//
//		if(ObjectUtils.isEmpty(errors)) {
//			log.error(FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL.getCause());
//			throw new FactoryException(FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL);
//		}
//		
//		for(MessageError errorParam: errors) {
//			MessageError error = MessageError.builder()
//					.message(errorParam.getMessage())
//					.messageKey(errorParam.getMessage())
//					.cause(errorParam.getCause())
//					.causeKey(errorParam.getCauseKey())
//					.build();
//			this.message.getErrors().add(error);
//		}
//
//		return this;
//	}
//	
//	public MessageBuilder withComments(final List<Comment> comments) {
//		
//		if(ObjectUtils.isEmpty(comments)) {
//			log.error(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL.getCause());
//			throw new FactoryException(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL);
//		}
//
//		for(Comment commentParam : comments) {
//			Comment comment = Comment.builder()
//					.message(commentParam.getMessage())
//					.messageKey(commentParam.getMessageKey())
//					.build();
//			
//			this.message.getComments().add(comment);
//		}
//		
//		return this;
//	}
//	
//	public MessageBuilder withComments(final EnumInterface... comments) {
//
//		if(ObjectUtils.isEmpty(comments)) {
//			log.error(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL.getCause());
//			throw new FactoryException(FactoryExceptionEnum.COMMENTS_CANNOT_BE_NULL);
//		}
//		
//		for(EnumInterface commentParam : comments) {
//			Comment comment = Comment.builder()
//					.message(commentParam.getValue())
//					.messageKey(commentParam.getValueKey())
//					.build();
//			this.message.getComments().add(comment);
//		}
//
//		return this;
//	}
//
//	public Message build() {
//		return this.message;
//	}
//
//}
