package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.ClientSimpleDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.domain.entities.Client;

@Mapper(componentModel="spring")
public interface ClientMapper extends AbstractMapper<Client, ClientDto>{
	ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

	ClientSimpleDto toClientSimpleDto (Client client);

}
