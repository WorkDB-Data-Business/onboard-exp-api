package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;

import br.com.harvest.onboardexperience.domain.dto.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;

@Mapper(componentModel="spring")
public interface CompanyRoleMapper extends AbstractMapper<CompanyRole, CompanyRoleDto>{

}
