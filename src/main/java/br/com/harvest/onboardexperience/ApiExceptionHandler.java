package br.com.harvest.onboardexperience;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import br.com.harvest.onboardexperience.domain.exceptions.*;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.harvest.onboardexperience.builders.MessageBuilder;
import br.com.harvest.onboardexperience.domain.dtos.responses.Message;
import br.com.harvest.onboardexperience.domain.dtos.responses.MessageError;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(FactoryException.class)
	public ResponseEntity<?> handleFactoryException(FactoryException e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(e);
	}
	
	@ExceptionHandler({
			BusinessException.class, UserAlreadyExistsException.class, CoinAlreadyExistsException.class,
			ClientAlreadyExistsException.class, CompanyRoleAlreadyExistsException.class,
			RewardAlreadyExistsException.class, FileAlreadyExistsException.class
	})
	public ResponseEntity<?> handleBusinessException(BusinessException e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(e);
	}

	@ExceptionHandler(Exception.class)
	public ResponseStatusException handleException(Exception e){
		logger.error(e);
		throw new ResponseStatusException(
				HttpStatus.INTERNAL_SERVER_ERROR,
				e.getMessage(),
				e.getCause()
		);
	}
	
	@ExceptionHandler({CompanyRoleNotFoundException.class, UserNotFoundException.class, CoinNotFoundException.class,
	ClientNotFoundException.class, RewardNotFoundException.class, PermissionNotFoundException.class,
	GroupNotFoundException.class, PasswordResetTokenNotFoundException.class})
	public ResponseEntity<?> handleNotFoundException(SubdomainNotFoundException e){
		logger.error(e);
		var message = new MessageBuilder().addMessage(e.getMessage()).withError(e.getMessage(), e.getMessage()).build();
								  
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(message);
	}
	
	@ExceptionHandler(PasswordResetTokenExpiredException.class)
	public ResponseEntity<?> handleAlreadyExpiredException(PasswordResetTokenExpiredException e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(e);
	}

	@ExceptionHandler(InsufficientCoinException.class)
	public ResponseEntity<?> handleInsufficientCoinException(InsufficientCoinException e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(e);
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
		
		  
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(message);
	}

}
