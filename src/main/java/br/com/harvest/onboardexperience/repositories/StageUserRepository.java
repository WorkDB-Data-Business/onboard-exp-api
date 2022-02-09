package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.StageUser;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.entities.keys.StageUserId;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StageUserRepository extends JpaRepository<StageUser, StageUserId>, JpaSpecificationExecutor<StageUser> {

    static Specification<StageUser> byUserAndTrail(@NonNull User user, @NonNull Trail trail) {
        return (stageUser, cq, cb) -> cb.and(
                cb.equal(stageUser.get("user"), user),
                cb.equal(stageUser.get("stage").get("trail"), trail)
        );
    }

}
