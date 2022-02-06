package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.HarvestFileMediaStage;
import br.com.harvest.onboardexperience.domain.entities.keys.HarvestFileMediaStageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HarvestFileMediaStageRepository extends JpaRepository<HarvestFileMediaStage, HarvestFileMediaStageId> {

}
