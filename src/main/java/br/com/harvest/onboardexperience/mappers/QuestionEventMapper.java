package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.entities.QuestionEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionEventMapper extends AbstractMapper<QuestionEvent, QuestionEventDto> {

    QuestionEventMapper INSTANCE = Mappers.getMapper(QuestionEventMapper.class);
}
