package br.com.harvest.onboardexperience.domain.exceptions;

public class ForbiddenAccess extends RuntimeException {
    
    private static final long serialVersionUID = 3528466777307424165L;

    public ForbiddenAccess(String message) {
        super(message);
    }

    public ForbiddenAccess(String message, Throwable cause) {
        super(message, cause, false, true);
    }
}
