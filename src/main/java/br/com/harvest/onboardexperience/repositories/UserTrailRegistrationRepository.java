package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.UserTrailRegistration;
import br.com.harvest.onboardexperience.domain.entities.keys.UserTrailRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTrailRegistrationRepository extends JpaRepository<UserTrailRegistration, UserTrailRegistrationId> {

}
