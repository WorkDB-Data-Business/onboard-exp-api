package br.com.harvest.onboardexperience.utils;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.harvest.onboardexperience.domain.exceptions.SubdomainNotFoundException;

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

	public static Boolean stringNullOrEmpty(String val) {
		return (val == null || val.trim().isEmpty() );
	}

	public static String formatNameToUsername(String name) {
		return name.toLowerCase().replace(" ", ".");
	}
	
}
