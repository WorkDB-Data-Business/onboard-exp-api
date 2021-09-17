package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.entities.User;

@Mapper(componentModel="spring")
public interface UserMapper extends AbstractMapper<User, UserDto>{
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "client", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	void updateEntity(UserDto dto, @MappingTarget User entity);

}
