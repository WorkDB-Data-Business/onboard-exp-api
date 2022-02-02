package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.forms.PositionForm;
import br.com.harvest.onboardexperience.domain.entities.Position;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PositionMapper extends AbstractMapper<Position, PositionForm> {

    PositionMapper INSTANCE = Mappers.getMapper(PositionMapper.class);

}
