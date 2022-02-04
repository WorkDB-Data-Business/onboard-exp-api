package br.com.harvest.onboardexperience.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    private static final long serialVersionUID = -4259843285202490608L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entity, String field, String value) {
        super(MessageFormat.format("The {0} with field {1} {2} not found or you don't have access to it.", entity, field, value));
    }
}
