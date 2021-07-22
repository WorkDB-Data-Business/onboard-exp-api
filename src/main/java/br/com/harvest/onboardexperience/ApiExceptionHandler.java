package br.com.harvest.onboardexperience;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.harvest.onboardexperience.builders.MessageBuilder;
import br.com.harvest.onboardexperience.domain.dto.responses.Message;
import br.com.harvest.onboardexperience.domain.dto.responses.MessageError;
import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.domain.exceptions.CompanyRoleNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.FactoryException;
import br.com.harvest.onboardexperience.domain.exceptions.SubdomainNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.TenantForbiddenException;
import br.com.harvest.onboardexperience.domain.exceptions.UserAlreadyExistsException;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(FactoryException.class)
	public ResponseEntity<?> handleFactoryException(FactoryException e){							  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(e);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleFactoryException(BusinessException e){
								  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(e);
	}
	
	@ExceptionHandler(SubdomainNotFoundException.class)
	public ResponseEntity<?> handleSubdomainNotFoundException(SubdomainNotFoundException e){
		
		var message = new MessageBuilder().addMessage(e.getMessage()).withError(e.getMessage(), e.getCause().getMessage()).build();
								  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(message);
	}
	
	@ExceptionHandler({CompanyRoleNotFoundException.class})
	public ResponseEntity<?> handleNotFoundException(SubdomainNotFoundException e){
		
		var message = new MessageBuilder().addMessage(e.getMessage()).withError(e.getMessage(), e.getMessage()).build();
								  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(message);
	}
	
	@ExceptionHandler(TenantForbiddenException.class)
	public ResponseEntity<?> handleTenantForbiddenException(TenantForbiddenException e){
		
		var message = new MessageBuilder().addMessage(e.getMessage()).withError(e.getMessage(), e.getCause().getMessage()).build();
								  
		return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(message);
	}
	
	//TODO: need to verify why this is not working xP
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleAlreadyExistsException(UserAlreadyExistsException e){
								  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(e);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<MessageError> errors = new ArrayList<>();
		
		for(FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			MessageError error = MessageError.builder().message("The value " + fieldError.getRejectedValue()
			+ " is invalid on field " + fieldError.getField() + ".")
			.cause(fieldError.getDefaultMessage()).build();
			errors.add(error);
		}
		
		Message message = new MessageBuilder().addMessage("The object " + e.getBindingResult().getObjectName() + " have validation errors.")
				.withErrors(errors)
				.withComments("Please, fix the errors and try again.")
				.build();
		
		  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(message);
	}
	
	
}
