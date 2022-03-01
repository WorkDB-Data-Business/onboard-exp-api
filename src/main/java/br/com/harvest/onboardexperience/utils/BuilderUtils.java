package br.com.harvest.onboardexperience.utils;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import br.com.harvest.onboardexperience.domain.exceptions.FactoryException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BuilderUtils {
	
	private BuilderUtils() {}
	
	
	public static void validateNullListObject(List<?> objects, String message) {
		if(ObjectUtils.isEmpty(objects)) {
			log.error(message + " collection cannot be null or empty", message + " collection is null or empty");
			throw new FactoryException(message + " collection is null or empty");
		}
	}
	
	public static void validateNullListObject(Object[] objects, String message) {
		if(ObjectUtils.isEmpty(objects)) {
			log.error(message + " collection cannot be null or empty", message + " collection is null or empty");
			throw new FactoryException(message + " collection is null or empty");
		}
	}

	public static void validateNullObject(Object object, String message) {
		if(ObjectUtils.isEmpty(object)) {
			log.error(message + " object cannot be null or empty", message + " object is null or empty");
			throw new FactoryException(message + " object is null or empty");
		}
	}
}
