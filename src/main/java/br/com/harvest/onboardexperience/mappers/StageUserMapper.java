package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.StageUserDTO;
import br.com.harvest.onboardexperience.domain.dtos.StageUserSimpleDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.entities.Position;
import br.com.harvest.onboardexperience.domain.entities.StageUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StageUserMapper extends AbstractMapper<StageUser, StageUserDTO> {

    StageUserMapper INSTANCE = Mappers.getMapper(StageUserMapper.class);

    StageUserSimpleDTO toSimpleDTO(StageUser stageUser);

}
