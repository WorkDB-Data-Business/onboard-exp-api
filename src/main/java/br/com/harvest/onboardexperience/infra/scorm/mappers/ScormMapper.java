package br.com.harvest.onboardexperience.infra.scorm.mappers;

import br.com.harvest.onboardexperience.infra.scorm.dtos.ScormDto;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface ScormMapper extends AbstractMapper<Scorm, ScormDto> {
    ScormMapper INSTANCE = Mappers.getMapper(ScormMapper.class);
}
