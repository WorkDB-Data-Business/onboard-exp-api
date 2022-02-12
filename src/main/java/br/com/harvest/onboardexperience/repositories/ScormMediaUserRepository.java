package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.ScormMediaStage;
import br.com.harvest.onboardexperience.domain.entities.ScormMediaUser;
import br.com.harvest.onboardexperience.domain.entities.keys.ScormMediaUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScormMediaUserRepository extends JpaRepository<ScormMediaUser, ScormMediaUserId> {

    Boolean existsByScormMedia(ScormMediaStage scormMediaStage);

}
