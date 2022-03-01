package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.ScormMediaStage;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.entities.keys.ScormMediaStageId;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScormMediaStageRepository extends JpaRepository<ScormMediaStage, ScormMediaStageId>, JpaSpecificationExecutor<ScormMediaStage> {

    Integer countByScorm(Scorm scorm);

    static Specification<ScormMediaStage> byStageAndScorm(@NonNull Stage stage, @NonNull Scorm scorm) {
        return (scormMedia, cq, cb) -> cb.and(
                cb.equal(scormMedia.get("scorm"), scorm),
                cb.equal(scormMedia.get("stage"), stage)
        );
    }

}
