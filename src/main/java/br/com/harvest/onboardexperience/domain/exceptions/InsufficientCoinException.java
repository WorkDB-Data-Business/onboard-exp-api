package br.com.harvest.onboardexperience.domain.exceptions;

public class InsufficientCoinException extends RuntimeException {
    
    private static final long serialVersionUID = -1157240362228668764L;

    public InsufficientCoinException(String message) {
        super(message);
    }

    public InsufficientCoinException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientCoinException(String message, String customCause) {
        super(message);
    }
    
}
