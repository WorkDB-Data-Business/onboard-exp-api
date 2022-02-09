package br.com.harvest.onboardexperience.repositories;


import br.com.harvest.onboardexperience.domain.entities.Position;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long>, JpaSpecificationExecutor<Stage> {


    static Specification<Stage> byIdAndTrail(@NonNull Long stageId, @NonNull Trail trail) {
        return (stage, cq, cb) -> cb.and(
                cb.equal(stage.get("id"), stageId),
                cb.equal(stage.get("trail"), trail)
        );
    }

    static Specification<Stage> byNameAndTrail(@NonNull String name, @NonNull Trail trail) {
        return (stage, cq, cb) -> cb.and(
                cb.equal(cb.lower(stage.get("id")), name.toLowerCase()),
                cb.equal(stage.get("trail"), trail)
        );
    }

    static Specification<Stage> byPositionAndTrail(@NonNull Position position, @NonNull Trail trail) {
        return (stage, cq, cb) -> cb.and(
                cb.equal(stage.get("position"), position),
                cb.equal(stage.get("trail"), trail)
        );
    }

}
