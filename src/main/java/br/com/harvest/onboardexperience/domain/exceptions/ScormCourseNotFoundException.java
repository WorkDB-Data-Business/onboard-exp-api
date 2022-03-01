package br.com.harvest.onboardexperience.domain.exceptions;

import java.text.MessageFormat;

public class ScormCourseNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8009423797853947807L;

    public ScormCourseNotFoundException(String field, String value) {
        super(MessageFormat.format("Scorm Course with {0} {1} not found.", field, value));
    }

    public ScormCourseNotFoundException(String field) {
        super(field);
    }

    public ScormCourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
