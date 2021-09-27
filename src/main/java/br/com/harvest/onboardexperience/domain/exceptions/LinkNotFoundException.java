package br.com.harvest.onboardexperience.domain.exceptions;

public class LinkNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3579652789519592683L;

    public LinkNotFoundException(String message) {
        super(message);
    }

    public LinkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LinkNotFoundException(String message, String customCause) {
        super(message);
    }
}
