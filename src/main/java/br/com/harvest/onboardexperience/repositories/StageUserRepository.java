package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.StageUser;
import br.com.harvest.onboardexperience.domain.entities.keys.StageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageUserRepository extends JpaRepository<StageUser, StageUserId> {

}
