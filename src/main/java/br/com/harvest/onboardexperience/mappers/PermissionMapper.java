package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.dtos.PermissionDto;
import br.com.harvest.onboardexperience.domain.entities.Permission;

@Mapper(componentModel="spring")
public interface PermissionMapper extends AbstractMapper<Permission, PermissionDto>{
	PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
}
