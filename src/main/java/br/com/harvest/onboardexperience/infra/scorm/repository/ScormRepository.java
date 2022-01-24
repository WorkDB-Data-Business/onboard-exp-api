package br.com.harvest.onboardexperience.infra.scorm.repository;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ScormRepository extends JpaRepository<Scorm, String>, JpaSpecificationExecutor<Scorm> {

    Optional<Scorm> findByIdAndClient(String id, Client client);

    static Specification<Scorm> betweenSinceAndUntil(LocalDateTime since, LocalDateTime until) {
        return (scorm, cq, cb) -> cb.between(scorm.get("created"), since, until);
    }

    static Specification<Scorm> byClient(Client client) {
        return (scorm, cq, cb) -> cb.equal(scorm.get("client"), client);
    }
}
