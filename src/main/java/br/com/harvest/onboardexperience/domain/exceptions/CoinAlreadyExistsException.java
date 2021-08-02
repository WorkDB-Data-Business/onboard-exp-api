package br.com.harvest.onboardexperience.domain.exceptions;

public class CoinAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 8579026343617010883L;
	
	private static final String COIN_ALREADY_EXISTS = "Coin already exists";

	public CoinAlreadyExistsException(String message) {
		super(COIN_ALREADY_EXISTS + ": " + message);
	}

	public CoinAlreadyExistsException(String message, Throwable cause) {
		super(COIN_ALREADY_EXISTS + ": " + message, cause);
	}

	public CoinAlreadyExistsException(String message, String customCause) {
		super(COIN_ALREADY_EXISTS + ": " +  message);
	}	


}
