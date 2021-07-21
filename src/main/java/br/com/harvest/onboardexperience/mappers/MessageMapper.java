package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.harvest.onboardexperience.domain.dto.responses.Message;
import br.com.harvest.onboardexperience.domain.dto.responses.SimpleMessage;
import br.com.harvest.onboardexperience.domain.dto.responses.SimpleMessageWithComment;

@Mapper(componentModel="spring")
public interface MessageMapper {
	MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
	
	SimpleMessage toSimpleMessage(Message message);
	
	SimpleMessageWithComment toSimpleMessageWithComment(Message message);

}
