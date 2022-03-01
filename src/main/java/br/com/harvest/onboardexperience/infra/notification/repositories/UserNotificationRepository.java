package br.com.harvest.onboardexperience.infra.notification.repositories;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.notification.entities.UserNotification;
import br.com.harvest.onboardexperience.infra.notification.entities.UserNotificationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationKey> {

    List<UserNotification> findAllByUser(User user);

    Optional<UserNotification> findByNotification_IdAndUser_Id(Long idNotification, Long idUser);

}
