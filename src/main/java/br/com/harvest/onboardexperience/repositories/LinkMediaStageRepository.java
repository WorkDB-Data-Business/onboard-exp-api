package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.LinkMediaStage;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.entities.keys.LinkMediaStageId;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkMediaStageRepository extends JpaRepository<LinkMediaStage, LinkMediaStageId>, JpaSpecificationExecutor<LinkMediaStage> {

    Integer countByLink(Link link);

    static Specification<LinkMediaStage> byStageAndLink(@NonNull Stage stage, @NonNull Link linkParam) {
        return (link, cq, cb) -> cb.and(
                cb.equal(link.get("link"), linkParam),
                cb.equal(link.get("stage"), stage)
        );
    }

}
