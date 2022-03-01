package br.com.harvest.onboardexperience.domain.exceptions;

public class AnswerQuestionNotFoundExecption extends RuntimeException{

    private static final long serialVersionUID = -620280818373790348L;

    private static final String Answer_NOT_FOUND = "Stage not found";

    public AnswerQuestionNotFoundExecption(String message) {
        super(Answer_NOT_FOUND + ": " + message);
    }

    public AnswerQuestionNotFoundExecption(String message, Throwable cause) {
        super(Answer_NOT_FOUND + ": " + message, cause);
    }

    public AnswerQuestionNotFoundExecption(String message, String customCause) {
        super(Answer_NOT_FOUND + ": " +  message);
    }


}
