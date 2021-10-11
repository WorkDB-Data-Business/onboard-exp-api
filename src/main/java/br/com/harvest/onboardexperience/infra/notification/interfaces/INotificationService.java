package br.com.harvest.onboardexperience.infra.notification.interfaces;

import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationForm;
import lombok.NonNull;

public interface INotificationService {

    void sendNotification(@NonNull NotificationForm form);

}
