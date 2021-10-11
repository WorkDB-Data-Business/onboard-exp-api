package br.com.harvest.onboardexperience.infra.notification.services;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationDto;
import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationForm;
import br.com.harvest.onboardexperience.infra.notification.entities.Notification;
import br.com.harvest.onboardexperience.infra.notification.entities.UserNotification;
import br.com.harvest.onboardexperience.infra.notification.entities.UserNotificationKey;
import br.com.harvest.onboardexperience.infra.notification.interfaces.INotificationService;
import br.com.harvest.onboardexperience.infra.notification.repositories.NotificationRepository;
import br.com.harvest.onboardexperience.infra.notification.repositories.UserNotificationRepository;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private FetchService fetchService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private final Long HARVEST_USER = 1L;

    private Predicate<UserNotification> getOnlyNotNull = (userNotification) -> Objects.nonNull(userNotification);

    private Function<UserNotification, UserNotification> visualizeNotification =
            (userNotification) -> { userNotification.setWasVisualized(true); return userNotification;};

    @Override
    public void sendNotification(@NonNull NotificationForm form) {
        userNotificationRepository.saveAll(generateUserNotifications(form.getUsers(), form.getText(), form.getAuthor()));
    }

    public void sendNotificationToAllUsersFromClients(@NonNull NotificationForm form){
        if(ObjectUtils.isNotEmpty(form.getClients())){
            userNotificationRepository.saveAll(
                    generateUserNotifications(getAllUsersFromClients(form.getClients()), form.getText(),
                            form.getAuthor()));
        }
    }

    public List<Long> getAllUsersFromClients(List<Long> clientsId){
        List<Long> allUsers = new ArrayList<>();

        fetchService.fetchClients(clientsId)
                .stream()
                .forEach(client -> allUsers.addAll(GenericUtils.extractIDsFromList(userRepository.findAllByClient(client),
                        User.class)));
        return allUsers;
    }

    public List<NotificationDto> getAllNotificationsFromUser(@NonNull String token){
        User user = fetchService.fetchUser(jwtTokenUtils.getUserId(token), token);

        List<UserNotification> notifications = userNotificationRepository.findAllByUser(user);

        return notifications.stream().map(NotificationService::userNotificationtoNotificationDto).collect(Collectors.toList());
    }

    public void visualizeNotification(@NonNull Long idNotification, @NonNull String token){

        UserNotification userNotification = userNotificationRepository.findByNotification_IdAndUser_Id(idNotification,
                jwtTokenUtils.getUserId(token))
                .orElse(null);

        userNotification.setWasVisualized(true);
        userNotificationRepository.save(userNotification);
    }

    public void visualizeAllNotifications(@NonNull String token){
        User user = fetchService.fetchUser(jwtTokenUtils.getUserId(token));
        List<UserNotification> allNotifications =
                userNotificationRepository.findAllByUser(user)
                .stream()
                        .filter(getOnlyNotNull)
                .map(visualizeNotification)
                .collect(Collectors.toList());
        userNotificationRepository.saveAll(allNotifications);
    }

    private static NotificationDto userNotificationtoNotificationDto(UserNotification userNotification){
        return NotificationDto.builder().id(userNotification.getNotification().getId())
                .sentAt(userNotification.getNotification().getSentAt())
                .text(userNotification.getNotification().getText())
                .author(UserMapper.INSTANCE.toUserSimpleDto(userNotification.getUser()))
                .wasVisualized(userNotification.getWasVisualized())
                .build();
    }

    private User getAuthorIfNotExists(Long author){
        if(Objects.isNull(author)){
            return fetchService.fetchUser(HARVEST_USER);
        }
        return fetchService.fetchUser(author);
    }

    private Notification convertFormToNotificationAndSave(@NonNull String text, Long author){
        return notificationRepository.save(Notification.builder()
                .author(getAuthorIfNotExists(author))
                .text(text)
                .build());
    }

    private UserNotification generateUserNotification(@NonNull String text, @NonNull Long receiver, Long author){
        Notification notification = convertFormToNotificationAndSave(text, author);
        User user = fetchService.fetchUser(receiver);
        return UserNotification.builder()
                .id(generateUserNotificationKey(user, notification))
                .notification(notification)
                .user(user)
                .wasVisualized(false)
                .build();
    }

    private List<UserNotification> generateUserNotifications(@NonNull List<Long> receivers, String text, Long author){
        List<UserNotification> userNotifications = new ArrayList<>();

        if(ObjectUtils.isNotEmpty(receivers)){
            for(Long idUser : receivers){
                userNotifications.add(generateUserNotification(text, idUser, author));
            }
        }

        return userNotifications;
    }

    private UserNotificationKey generateUserNotificationKey(@NonNull User user, @NonNull Notification notification){
        return UserNotificationKey.builder()
                .idNotification(notification.getId())
                .idUser(user.getId()).build();
    }


}
