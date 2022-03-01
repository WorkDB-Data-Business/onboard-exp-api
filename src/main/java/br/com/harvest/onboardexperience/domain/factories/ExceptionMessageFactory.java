package br.com.harvest.onboardexperience.domain.factories;

public class ExceptionMessageFactory {
	
	private static final String ALREADY_EXISTS = "already exists";
	private static final String NOT_FOUND = "not found";
	
	public static String createNotFoundMessage(String domainName, String field, String value) {
		return createMessage(domainName, field, value, NOT_FOUND);
	}
	
	public static String createAlreadyExistsMessage(String domainName, String field, String value) {
		return createMessage(domainName, field, value, ALREADY_EXISTS);
	}
	
	private static String createMessage(String domainName, String field, String value, String errorType) {
		StringBuilder builder = new StringBuilder("The ").append(domainName)
				.append(" with ")
				.append(field)
				.append(" ")
				.append(value)
				.append(" ")
				.append(errorType)
				.append(".");
		
		return builder.toString();
	}

}
