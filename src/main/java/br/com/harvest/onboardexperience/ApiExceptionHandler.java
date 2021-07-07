package br.com.harvest.onboardexperience;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.harvest.onboardexperience.domain.dto.Comment;
import br.com.harvest.onboardexperience.domain.dto.Message;
import br.com.harvest.onboardexperience.domain.dto.MessageError;
import br.com.harvest.onboardexperience.domain.exception.FactoryException;
import br.com.harvest.onboardexperience.domain.exception.enumerators.ExceptionEnum;
import br.com.harvest.onboardexperience.domain.exception.enumerators.FactoryExceptionEnum;
import br.com.harvest.onboardexperience.domain.factory.MessageCommentFactory;
import br.com.harvest.onboardexperience.domain.factory.MessageErrorFactory;
import br.com.harvest.onboardexperience.domain.factory.MessageFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {
	
	@ExceptionHandler(FactoryException.class)
	public ResponseEntity<Message> handleFactoryException(FactoryException e){
		
		
		List<MessageError> errors = new MessageErrorFactory().buildError(e.getException().getValue(),
				e.getException().getCause(),
				e.getException().getValueKey(),
				e.getException().getCauseKey()).build();
		
		List<Comment> comments = new MessageCommentFactory()
				.buildComment(ExceptionEnum.INTERNAL_SERVER_GENERIC_ERROR.getCause(), ExceptionEnum.INTERNAL_SERVER_GENERIC_ERROR.getCauseKey()).build();
		
		var message = new MessageFactory().buildMessage(FactoryExceptionEnum.GENERIC_ERROR_MESSAGE_FACTORY.getValue())
										  .setMessageKey(FactoryExceptionEnum.GENERIC_ERROR_MESSAGE_FACTORY.getValueKey())
										  .withErrors(errors)
										  .withComments(comments)
										  .build();
										  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(message);
	}

}
