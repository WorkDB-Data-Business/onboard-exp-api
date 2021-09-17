package br.com.harvest.onboardexperience.domain.exceptions;

public class PasswordResetTokenExpiredException extends RuntimeException {

    private static final long serialVersionUID = 4284903158533002405L;

    private static final String PASSWORD_RESET_TOKEN_ALREADY_EXPIRED = "The password reset token is already expired";

    public PasswordResetTokenExpiredException(String message) {
        super(PASSWORD_RESET_TOKEN_ALREADY_EXPIRED + ": " + message);
    }

    public PasswordResetTokenExpiredException(String message, Throwable cause) {
        super(PASSWORD_RESET_TOKEN_ALREADY_EXPIRED + ": " + message, cause);
    }

    public PasswordResetTokenExpiredException(String message, String customCause) {
        super(PASSWORD_RESET_TOKEN_ALREADY_EXPIRED + ": " +  message);
    }
}
