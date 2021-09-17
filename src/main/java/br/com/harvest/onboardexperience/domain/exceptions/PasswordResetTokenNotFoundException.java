package br.com.harvest.onboardexperience.domain.exceptions;

public class PasswordResetTokenNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7290504701682759053L;

    private static final String PASSWORD_RESET_TOKEN_NOT_FOUND = "The password reset token is invalid or was not found";

    public PasswordResetTokenNotFoundException(String message) {
        super(PASSWORD_RESET_TOKEN_NOT_FOUND + ": " + message);
    }

    public PasswordResetTokenNotFoundException(String message, Throwable cause) {
        super(PASSWORD_RESET_TOKEN_NOT_FOUND + ": " + message, cause);
    }

    public PasswordResetTokenNotFoundException(String message, String customCause) {
        super(PASSWORD_RESET_TOKEN_NOT_FOUND + ": " +  message);
    }
}
