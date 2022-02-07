package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.AnswerDescriptiveDto;
import br.com.harvest.onboardexperience.domain.entities.AnswerDescriptive;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerDescriptiveMapper extends AbstractMapper<AnswerDescriptive, AnswerDescriptiveDto>{

    AnswerDescriptiveMapper INSTANCE = Mappers.getMapper(AnswerDescriptiveMapper.class);
}
