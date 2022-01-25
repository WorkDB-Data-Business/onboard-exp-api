package br.com.harvest.onboardexperience.infra.scorm.mappers;

import br.com.harvest.onboardexperience.infra.scorm.dtos.ScormRegistrationDto;
import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import br.com.harvest.onboardexperience.mappers.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ScormRegistrationMapper extends AbstractMapper<ScormRegistration, ScormRegistrationDto> {

}
