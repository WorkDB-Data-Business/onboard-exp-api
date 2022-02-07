package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.TrailSimpleDTO;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface TrailMapper extends AbstractMapper<Trail, TrailDTO> {

    TrailMapper INSTANCE = Mappers.getMapper(TrailMapper.class);

    TrailSimpleDTO toSimpleDto(Trail trail);
}
