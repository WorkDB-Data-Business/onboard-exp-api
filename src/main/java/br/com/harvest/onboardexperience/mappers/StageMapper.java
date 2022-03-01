package br.com.harvest.onboardexperience.mappers;


import br.com.harvest.onboardexperience.domain.dtos.StageDTO;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StageMapper  extends AbstractMapper<Stage, StageDTO>{

    StageMapper INSTANCE = Mappers.getMapper(StageMapper.class);
}