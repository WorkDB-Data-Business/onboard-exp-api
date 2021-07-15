package br.com.harvest.onboardexperience.utils;

import java.util.List;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;

public class GenericUtils {
	
	private GenericUtils() {
		
	}
	
	public static Boolean validateCPF(String cpf) {
		CPFValidator cpfValidator = new CPFValidator();
		List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf);
		
		if(!erros.isEmpty()) return false; 
		
		return true;
	}

}
