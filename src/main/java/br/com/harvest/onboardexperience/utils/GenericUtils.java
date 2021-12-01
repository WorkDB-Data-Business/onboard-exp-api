package br.com.harvest.onboardexperience.utils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.ReflectionUtils;

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

	public static Object executeMethodFromGenericClass(Class<?> clazz, String methodName, Optional<?> targetObject){
		if(Objects.isNull(clazz) || ObjectUtils.isEmpty(methodName) || Objects.isNull(targetObject)){
			return null;
		}

		Method method = ReflectionUtils.findMethod(clazz, methodName);
		return Objects.nonNull(method) && targetObject.isPresent()
				? ReflectionUtils.invokeMethod(method, targetObject.get())
				: null;
	}

	public static Boolean stringNullOrEmpty(String val) {
		return (val == null || val.trim().isEmpty() );
	}

	public static String formatNameToUsername(String name) {
		return name.toLowerCase().replace(" ", ".");
	}

	public static Boolean checkIfOptionalHaveValue(Optional<?> optional){
		if(Objects.isNull(optional) || optional.isEmpty()){
			return false;
		}

		return true;
	}

	public static List<Long> extractIDsFromList(@NonNull List<?> objects, Class<?> clazz){
		if(ObjectUtils.isNotEmpty(objects)){
			return objects.stream().
					map(object ->
					executeMethodFromGenericClass(clazz, "getId", Optional.of(object)))
					.map(objectString -> objectString.toString())
					.mapToLong(id -> Long.parseLong(id))
					.boxed()
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}
