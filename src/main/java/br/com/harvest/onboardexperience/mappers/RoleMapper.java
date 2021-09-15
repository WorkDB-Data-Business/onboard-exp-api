package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.dtos.RoleDto;
import br.com.harvest.onboardexperience.domain.entities.Role;

@Mapper(componentModel="spring")
public interface RoleMapper extends AbstractMapper<Role, RoleDto>{
	RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
}
