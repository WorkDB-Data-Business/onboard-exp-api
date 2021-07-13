package br.com.harvest.onboardexperience.domain.dto.response.templates;

import br.com.harvest.onboardexperience.domain.builders.ErrorAbstractBuilder;
import br.com.harvest.onboardexperience.domain.enumerators.interfaces.EnumInterface;
import br.com.harvest.onboardexperience.domain.exceptions.enumerators.ExceptionEnum;

public class InternalServerErrorMessage extends ErrorAbstractBuilder {
	
	@Override
	protected EnumInterface getMessage() {
		return ExceptionEnum.INTERNAL_SERVER_GENERIC_ERROR;
	}
}
