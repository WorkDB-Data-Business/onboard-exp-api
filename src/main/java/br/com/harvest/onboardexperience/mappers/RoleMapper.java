package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;

import br.com.harvest.onboardexperience.domain.dto.RoleDto;
import br.com.harvest.onboardexperience.domain.entities.Role;

@Mapper(componentModel="spring")
public interface RoleMapper extends AbstractMapper<Role, RoleDto>{
	
}
