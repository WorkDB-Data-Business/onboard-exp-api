package br.com.harvest.onboardexperience.domain.exceptions;

public class CompanyRoleAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 2743207502048885819L;

    private static final String COMPANY_ROLE_ALREADY_EXISTS = "Company role already exists";

    public CompanyRoleAlreadyExistsException(String message) {
        super(COMPANY_ROLE_ALREADY_EXISTS + ": " + message);
    }

    public CompanyRoleAlreadyExistsException(String message, Throwable cause) {
        super(COMPANY_ROLE_ALREADY_EXISTS + ": " + message, cause);
    }

    public CompanyRoleAlreadyExistsException(String message, String customCause) {
        super(COMPANY_ROLE_ALREADY_EXISTS + ": " +  message);
    }
}
