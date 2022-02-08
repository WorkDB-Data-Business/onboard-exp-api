package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.HarvestFileMediaUser;
import br.com.harvest.onboardexperience.domain.entities.keys.HarvestFileMediaUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HarvestFileMediaUserRepository extends JpaRepository<HarvestFileMediaUser, HarvestFileMediaUserId> {

}
