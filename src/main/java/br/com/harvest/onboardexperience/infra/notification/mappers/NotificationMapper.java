package br.com.harvest.onboardexperience.infra.notification.mappers;

import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationDto;
import br.com.harvest.onboardexperience.infra.notification.entities.Notification;
import br.com.harvest.onboardexperience.infra.notification.entities.UserNotification;
import br.com.harvest.onboardexperience.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface NotificationMapper extends AbstractMapper<Notification, NotificationDto> {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

}
