package br.com.harvest.onboardexperience.utils;

import java.util.List;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

public class GenericUtils {
	
	private GenericUtils() {}
	
	public static Boolean validateCPF(String cpf) {
		CPFValidator cpfValidator = new CPFValidator();
		List<ValidationMessage> errors = cpfValidator.invalidMessagesFor(cpf);
		
		if(!errors.isEmpty()) return false; 
		
		return true;
	}
	
	public static Boolean validateCNPJ(String cnpj) {
		CNPJValidator cnpjValidator = new CNPJValidator();
		List<ValidationMessage> errors = cnpjValidator.invalidMessagesFor(cnpj);
		
		if(!errors.isEmpty()) return false;
		
		return true;
	}
	

}
