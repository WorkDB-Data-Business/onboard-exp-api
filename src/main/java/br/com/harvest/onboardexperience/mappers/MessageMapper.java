package br.com.harvest.onboardexperience.mappers;

import org.mapstruct.Mapper;

import br.com.harvest.onboardexperience.domain.dto.responses.Message;
import br.com.harvest.onboardexperience.domain.dto.responses.SimpleMessage;
import br.com.harvest.onboardexperience.domain.dto.responses.SimpleMessageWithComment;

@Mapper(componentModel="spring")
public interface MessageMapper {
	
	SimpleMessage toSimpleMessage(Message message);
	
	SimpleMessageWithComment toSimpleMessageWithComment(Message message);

}
