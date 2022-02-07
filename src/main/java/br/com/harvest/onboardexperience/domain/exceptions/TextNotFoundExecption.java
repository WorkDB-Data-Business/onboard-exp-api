package br.com.harvest.onboardexperience.domain.exceptions;

public class TextNotFoundExecption extends RuntimeException{

    private static final long serialVersionUID = -620280818373790348L;

    private static final String TEXT_NOT_FOUND = "Stage not found";

    public TextNotFoundExecption(String message) {
        super(TEXT_NOT_FOUND + ": " + message);
    }

    public TextNotFoundExecption(String message, Throwable cause) {
        super(TEXT_NOT_FOUND + ": " + message, cause);
    }

    public TextNotFoundExecption(String message, String customCause) {
        super(TEXT_NOT_FOUND + ": " +  message);
    }


}
