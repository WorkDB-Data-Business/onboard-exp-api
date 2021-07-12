package br.com.harvest.onboardexperience;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import br.com.harvest.onboardexperience.domain.dto.response.templates.InternalServerErrorMessage;
import br.com.harvest.onboardexperience.domain.dto.responses.Message;
import br.com.harvest.onboardexperience.domain.exception.BusinessException;
import br.com.harvest.onboardexperience.domain.exception.FactoryException;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {
	
	@ExceptionHandler(FactoryException.class)
	public ResponseEntity<?> handleFactoryException(FactoryException e){
		
		Message message = new InternalServerErrorMessage()
				.setError(e.getException())
				.build();
								  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(message);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleFactoryException(BusinessException e){
		
		Message message = new InternalServerErrorMessage()
				.setError(e.getException())
				.build();
								  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(message);
	}

}
