package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.QuestionnaireDto;
import br.com.harvest.onboardexperience.domain.entities.Questionnaire;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionnaireMapper extends AbstractMapper<Questionnaire, QuestionnaireDto>{

    QuestionnaireMapper INSTANCE = Mappers.getMapper(QuestionnaireMapper.class);

}
