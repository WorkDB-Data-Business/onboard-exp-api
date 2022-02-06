package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.LinkMediaStage;
import br.com.harvest.onboardexperience.domain.entities.keys.LinkMediaStageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkMediaStageRepository extends JpaRepository<LinkMediaStage, LinkMediaStageId> {

}
