package br.com.harvest.onboardexperience.infra.storage.mappers;

import br.com.harvest.onboardexperience.infra.storage.dtos.LinkDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import br.com.harvest.onboardexperience.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface LinkMapper extends AbstractMapper<Link, LinkDto> {

    LinkMapper INSTANCE = Mappers.getMapper(LinkMapper.class);

    LinkSimpleDto toLinkSimpleDto (Link link);

}
