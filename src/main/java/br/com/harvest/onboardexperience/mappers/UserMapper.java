package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.dto.UserDto;
import br.com.harvest.onboardexperience.domain.entities.User;

@Mapper(componentModel="spring")
public interface UserMapper extends AbstractMapper<User, UserDto>{
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
