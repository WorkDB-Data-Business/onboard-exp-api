package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;

import br.com.harvest.onboardexperience.domain.dto.ClientDto;
import br.com.harvest.onboardexperience.domain.entities.Client;

@Mapper(componentModel="spring")
public interface ClientMapper extends AbstractMapper<Client, ClientDto>{

}
