package br.com.harvest.onboardexperience.domain.exceptions;

public class CoinNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4234125081813698771L;
	
	private static final String COIN_NOT_FOUND = "Coin not found";

	public CoinNotFoundException(String message) {
		super(COIN_NOT_FOUND + ": " + message);
	}

	public CoinNotFoundException(String message, Throwable cause) {
		super(COIN_NOT_FOUND + ": " + message, cause);
	}

	public CoinNotFoundException(String message, String customCause) {
		super(COIN_NOT_FOUND + ": " +  message);
	}	

}
