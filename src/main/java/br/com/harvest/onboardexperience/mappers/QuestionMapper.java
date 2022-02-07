package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.domain.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper extends AbstractMapper<Question, QuestionDto> {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
}
