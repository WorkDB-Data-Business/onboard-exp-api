package br.com.harvest.onboardexperience.domain.exceptions;

public class RewardAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -4114178921049307187L;

	private static final String REWARD_ALREADY_EXISTS = "Reward already exists";

	public RewardAlreadyExistsException(String message) {
		super(REWARD_ALREADY_EXISTS + ": " + message);
	}

	public RewardAlreadyExistsException(String message, Throwable cause) {
		super(REWARD_ALREADY_EXISTS + ": " + message, cause);
	}

	public RewardAlreadyExistsException(String message, String customCause) {
		super(REWARD_ALREADY_EXISTS + ": " +  message);
	}	
	
}
