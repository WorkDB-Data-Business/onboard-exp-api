package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.TextEventDto;
import br.com.harvest.onboardexperience.domain.entities.TextEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TextEventMapper extends AbstractMapper<TextEvent, TextEventDto> {

    TextEventMapper INSTANCE = Mappers.getMapper(TextEventMapper.class);
}
