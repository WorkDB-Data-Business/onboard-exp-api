package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.HarvestFileMediaStage;
import br.com.harvest.onboardexperience.domain.entities.HarvestFileMediaUser;
import br.com.harvest.onboardexperience.domain.entities.ScormMediaStage;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.entities.keys.HarvestFileMediaStageId;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HarvestFileMediaStageRepository extends JpaRepository<HarvestFileMediaStage, HarvestFileMediaStageId>, JpaSpecificationExecutor<HarvestFileMediaStage> {

    Integer countByHarvestFile(HarvestFile harvestFile);

    static Specification<HarvestFileMediaStage> byStageAndHarvestFile(@NonNull Stage stage, @NonNull HarvestFile harvestFile) {
        return (file, cq, cb) -> cb.and(
                cb.equal(file.get("harvestFile"), harvestFile),
                cb.equal(file.get("stage"), stage)
        );
    }

}
