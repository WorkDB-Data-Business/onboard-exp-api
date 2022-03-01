package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.HarvestFileMediaStageDTO;
import br.com.harvest.onboardexperience.domain.entities.HarvestFileMediaStage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface HarvestFileStageMapper extends AbstractMapper<HarvestFileMediaStage, HarvestFileMediaStageDTO>{
    HarvestFileStageMapper INSTANCE = Mappers.getMapper(HarvestFileStageMapper.class);

}
