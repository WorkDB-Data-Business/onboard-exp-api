package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Group;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    Page<Group> findAllByClient_Tenant(String tenant, Pageable pageable);

    Optional<Group> findByIdAndClient_Tenant(Long id, String tenant);

    Optional<Group> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);

    static Specification<Group> byName(@NonNull String name) {
        return (group, cq, cb) -> cb.like(cb.lower(group.get("name")), "%" + name.toLowerCase() + "%");
    }

    static Specification<Group> byIdAsString(@NonNull String id) {
        return (group, cq, cb) -> cb.like(cb.lower(group.get("id").as(String.class)), id + "%");
    }

    static Specification<Group> byCustomFilter(@NonNull String criteria) {
        return Specification.where(byIdAsString(criteria)).or(byName(criteria));
    }

    static Specification<Group> byClient(@NonNull Client client) {
        return (group, cq, cb) -> cb.equal(group.get("client"), client);
    }


}
