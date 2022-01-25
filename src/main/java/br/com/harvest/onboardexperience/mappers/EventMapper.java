package br.com.harvest.onboardexperience.mappers;


import br.com.harvest.onboardexperience.domain.dtos.EventDto;
import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.domain.entities.Event;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper extends AbstractMapper<Event, EventDto>{

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);
}