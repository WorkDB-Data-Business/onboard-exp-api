package br.com.harvest.onboardexperience.mappers;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerQuestionMapper extends AbstractMapper<AnswerQuestion, AnswerQuestionDto> {

    AnswerQuestionMapper INSTANCE = Mappers.getMapper(AnswerQuestionMapper.class);
}
