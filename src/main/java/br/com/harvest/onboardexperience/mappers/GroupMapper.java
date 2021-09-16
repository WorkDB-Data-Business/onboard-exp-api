package br.com.harvest.onboardexperience.mappers;

import br.com.harvest.onboardexperience.domain.dtos.GroupDto;
import br.com.harvest.onboardexperience.domain.dtos.GroupSimpleDto;
import br.com.harvest.onboardexperience.domain.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface GroupMapper extends AbstractMapper<Group, GroupDto>{
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupSimpleDto toGroupSimpleDto(Group group);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(GroupDto dto, @MappingTarget Group entity);

}
