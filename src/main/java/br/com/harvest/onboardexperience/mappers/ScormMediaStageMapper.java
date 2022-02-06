package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.ScormMediaStageDTO;
import br.com.harvest.onboardexperience.domain.entities.ScormMediaStage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScormMediaStageMapper extends AbstractMapper<ScormMediaStage, ScormMediaStageDTO>{

    ScormMediaStageMapper INSTANCE = Mappers.getMapper(ScormMediaStageMapper.class);

}
