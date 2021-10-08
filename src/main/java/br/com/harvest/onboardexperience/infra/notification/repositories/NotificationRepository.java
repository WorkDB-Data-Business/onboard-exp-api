package br.com.harvest.onboardexperience.infra.notification.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationDto;
import br.com.harvest.onboardexperience.infra.notification.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, NotificationDto> {

    List<Notification> findByUsers(User user);
    List<Notification> findByClients(Client client);
}
