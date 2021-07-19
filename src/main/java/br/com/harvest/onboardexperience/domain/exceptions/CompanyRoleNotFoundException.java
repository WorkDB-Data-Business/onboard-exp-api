package br.com.harvest.onboardexperience.domain.exceptions;

public class CompanyRoleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1336192881891972448L;
	
	private static final String COMPANY_ROLE_NOT_FOUND = "Company Role not found";

	public CompanyRoleNotFoundException(String message) {
		super(COMPANY_ROLE_NOT_FOUND + ": " + message);
	}

	public CompanyRoleNotFoundException(String message, Throwable cause) {
		super(COMPANY_ROLE_NOT_FOUND + ": " + message, cause);
	}

	public CompanyRoleNotFoundException(String message, String customCause) {
		super(COMPANY_ROLE_NOT_FOUND + ": " +  message);
	}	

	public static String buildMessage(String field, String value) {
		return "The company role with " + field + " " + value + " not found"; 
	}
}
