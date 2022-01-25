package br.com.harvest.onboardexperience.infra.storage.mappers;

import br.com.harvest.onboardexperience.infra.storage.dtos.ClientFileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.ClientFileSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.entities.ClientFile;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface ClientFileMapper extends AbstractMapper<ClientFile, ClientFileDto> {

    ClientFileMapper INSTANCE = Mappers.getMapper(ClientFileMapper.class);

    ClientFileSimpleDto toClientFileSimpleDto (ClientFile clientFile);

}
