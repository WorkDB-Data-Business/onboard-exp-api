package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;

import br.com.harvest.onboardexperience.domain.dto.PermissionDto;
import br.com.harvest.onboardexperience.domain.entities.Permission;

@Mapper(componentModel="spring")
public interface PermissionMapper extends AbstractMapper<Permission, PermissionDto>{

}
