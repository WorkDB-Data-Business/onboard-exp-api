package br.com.harvest.onboardexperience.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1488030285658715080L;

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String entity, String field, String value) {
        super(MessageFormat.format("The {0} with field {1} {2} already exists.", entity, field, value));
    }
}
