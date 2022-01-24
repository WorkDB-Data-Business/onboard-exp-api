package br.com.harvest.onboardexperience.infra.scorm.repository;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScormRegistrationRepository extends JpaRepository<ScormRegistration, String> {

    Optional<ScormRegistration> findByUserAndScormAndIsActive(User user, Scorm scorm, Boolean isActive);

}
