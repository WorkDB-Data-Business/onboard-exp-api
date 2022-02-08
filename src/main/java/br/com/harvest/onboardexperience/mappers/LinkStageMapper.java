package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.LinkMediaStageDTO;
import br.com.harvest.onboardexperience.domain.entities.LinkMediaStage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface LinkStageMapper extends AbstractMapper<LinkMediaStage, LinkMediaStageDTO>{
    LinkStageMapper INSTANCE = Mappers.getMapper(LinkStageMapper.class);
}
