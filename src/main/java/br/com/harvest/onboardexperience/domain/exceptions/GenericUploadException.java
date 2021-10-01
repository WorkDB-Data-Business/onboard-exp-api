package br.com.harvest.onboardexperience.domain.exceptions;

public class GenericUploadException extends RuntimeException {

    private static final long serialVersionUID = 5245282050732109039L;

    public GenericUploadException(String message) {
        super(message);
    }

    public GenericUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericUploadException(String message, String customCause) {
        super(message, new Throwable(customCause));
    }
}
