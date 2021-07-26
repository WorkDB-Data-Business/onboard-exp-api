package br.com.harvest.onboardexperience.domain.exceptions;

public class RewardNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -620280818373790348L;
	
	private static final String REWARD_NOT_FOUND = "Reward not found";

	public RewardNotFoundException(String message) {
		super(REWARD_NOT_FOUND + ": " + message);
	}

	public RewardNotFoundException(String message, Throwable cause) {
		super(REWARD_NOT_FOUND + ": " + message, cause);
	}

	public RewardNotFoundException(String message, String customCause) {
		super(REWARD_NOT_FOUND + ": " +  message);
	}	
	
}
