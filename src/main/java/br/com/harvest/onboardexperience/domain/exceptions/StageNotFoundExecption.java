package br.com.harvest.onboardexperience.domain.exceptions;

public class StageNotFoundExecption extends RuntimeException{

    private static final long serialVersionUID = -620280818373790348L;

    private static final String STAGE_NOT_FOUND = "Stage not found";

    public StageNotFoundExecption(String message) {
        super(STAGE_NOT_FOUND + ": " + message);
    }

    public StageNotFoundExecption(String message, Throwable cause) {
        super(STAGE_NOT_FOUND + ": " + message, cause);
    }

    public StageNotFoundExecption(String message, String customCause) {
        super(STAGE_NOT_FOUND + ": " +  message);
    }


}
