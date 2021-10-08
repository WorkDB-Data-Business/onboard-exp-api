package br.com.harvest.onboardexperience.infra.notification.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationDto;
import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationForm;
import br.com.harvest.onboardexperience.infra.notification.entities.Notification;
import br.com.harvest.onboardexperience.infra.notification.interfaces.INotificationService;
import br.com.harvest.onboardexperience.infra.notification.mappers.NotificationMapper;
import br.com.harvest.onboardexperience.infra.notification.repositories.NotificationRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private FetchService fetchService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private final Long HARVEST_USER = 1L;


    @Override
    public void sendNotification(@NonNull NotificationForm form) {
        notificationRepository.save(convertFormToNotification(form));
    }

    public List<NotificationDto> getAllNotificationsFromUser(@NonNull String token){
        User user = fetchService.fetchUser(jwtTokenUtils.getUserId(token), token);
        Client client = user.getClient();

        List<Notification> notifications = notificationRepository.findByUsers(user);
        notifications.addAll(notificationRepository.findByClients(client));

        return notifications.stream().map(NotificationMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    private User getAuthorIfNotExists(Long author){
        if(Objects.isNull(author)){
            return fetchService.fetchUser(HARVEST_USER);
        }
        return fetchService.fetchUser(author);
    }

    private Notification convertFormToNotification(@NonNull NotificationForm form){
        return Notification.builder()
                .author(getAuthorIfNotExists(form.getAuthor()))
                .users(fetchService.fetchUsers(form.getUsers()))
                .clients(fetchService.fetchClients(form.getClients()))
                .text(form.getText())
                .build();
    }


}
