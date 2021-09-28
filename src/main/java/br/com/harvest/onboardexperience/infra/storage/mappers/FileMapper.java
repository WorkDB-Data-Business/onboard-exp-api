package br.com.harvest.onboardexperience.infra.storage.mappers;

import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface FileMapper extends AbstractMapper<HarvestFile, FileDto> {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    FileSimpleDto toFileSimpleDto (HarvestFile harvestFile);

}
