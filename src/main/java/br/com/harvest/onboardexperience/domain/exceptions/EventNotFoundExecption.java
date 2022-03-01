package br.com.harvest.onboardexperience.domain.exceptions;

public class EventNotFoundExecption extends RuntimeException{

    private static final long serialVersionUID = -620280818373790348L;

    private static final String Event_NOT_FOUND = "Stage not found";

    public EventNotFoundExecption(String message) {
        super(Event_NOT_FOUND + ": " + message);
    }

    public EventNotFoundExecption(String message, Throwable cause) {
        super(Event_NOT_FOUND + ": " + message, cause);
    }

    public EventNotFoundExecption(String message, String customCause) {
        super(Event_NOT_FOUND + ": " +  message);
    }


}
