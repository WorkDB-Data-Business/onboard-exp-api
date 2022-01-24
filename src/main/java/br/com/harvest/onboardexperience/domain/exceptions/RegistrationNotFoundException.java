package br.com.harvest.onboardexperience.domain.exceptions;

public class RegistrationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5527140233772534576L;

    public RegistrationNotFoundException(String message) {
        super(message);
    }

    public RegistrationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
