package br.com.harvest.onboardexperience;

import java.io.FileNotFoundException;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
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
			InvalidCnpjException.class, InvalidCpfException.class, RewardAlreadyExistsException.class,
			FileAlreadyExistsException.class, InsufficientCoinException.class, PasswordResetTokenExpiredException.class,
			BadCredentialsException.class, UserBlockedException.class, UserDisabledException.class
	})
	public ResponseEntity<?> handleBadRequestException(Exception e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(e);
	}

	@ExceptionHandler({
		ForbiddenAccess.class
	})
	public ResponseEntity<?> handleForbiddenAccessException(Exception e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(e);
	}

	@ExceptionHandler({
			IllegalAccessException.class
	})
	public ResponseEntity<?> handleMethodNotAllowedException(Exception e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).contentType(MediaType.APPLICATION_JSON).body(e);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e){
		logger.error(e);
		throw new ResponseStatusException(
				HttpStatus.INTERNAL_SERVER_ERROR,
				e.getMessage(),
				e.getCause()
		);
	}
	
	@ExceptionHandler({CompanyRoleNotFoundException.class, UserNotFoundException.class, CoinNotFoundException.class,
	ClientNotFoundException.class, RewardNotFoundException.class, PermissionNotFoundException.class,
	GroupNotFoundException.class, PasswordResetTokenNotFoundException.class, FileNotFoundException.class})
	public ResponseEntity<?> handleNotFoundException(Exception e){
		logger.error(e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(e);
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
		
		Message message = new MessageBuilder().addMessage("The request has validation errors.")
				.withErrors(errors)
				.withComments("Please, fix the errors and try again.")
				.build();
		
		  
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(message);
	}

}
