package br.com.harvest.onboardexperience.infra.scorm.repository;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;
import java.time.LocalDateTime;

@Repository
public interface ScormRepository extends JpaRepository<Scorm, String>, JpaSpecificationExecutor<Scorm> {

    static Specification<Scorm> betweenSinceAndUntil(LocalDateTime since, LocalDateTime until) {
        return (scorm, cq, cb) -> cb.between(scorm.get("created"), since, until);
    }

    static Specification<Scorm> byScormIdLike(@NonNull String id) {
        return (scorm, cq, cb) -> cb.like(cb.lower(scorm.get("id")), id.toLowerCase() + "%");
    }

    static Specification<Scorm> byScormIdEqual(@NonNull String id) {
        return (scorm, cq, cb) -> cb.equal(scorm.get("id"), id);
    }

    static Specification<Scorm> byScormTitle(@NonNull String title) {
        return (scorm, cq, cb) -> cb.like(cb.lower(scorm.get("title")), "%" + title.toLowerCase() + "%");
    }

    static Specification<Scorm> byCustomFilter(@NonNull String customFilter){
        return Specification.where(
                byScormIdLike(customFilter))
                .or(byScormTitle(customFilter))
                .or(byAuthor(customFilter));
    }

    static Specification<Scorm> byAuthor(@NonNull String criteria) {
        return (scorm, cq, cb) -> cb.or(
                cb.like(cb.lower(scorm.get("author").get("firstName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(scorm.get("author").get("lastName")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(scorm.get("author").get("nickname")), "%" + criteria.toLowerCase() + "%"),
                cb.like(cb.lower(scorm.get("author").get("cpf")),  criteria.toLowerCase() + "%"),
                cb.like(cb.lower(scorm.get("author").get("email")), criteria.toLowerCase() + "%")
        );
    }

    static Specification<Scorm> byAuthor(@NonNull User author) {
        return (scorm, cq, cb) -> cb.equal(scorm.get("author"), author);
    }

    static Specification<Scorm> byScormLearningStandard(@NonNull CourseSchema.CourseLearningStandardEnum standard) {
        return (scorm, cq, cb) -> cb.equal(cb.lower(scorm.get("title")), standard);
    }

    static Specification<Scorm> byAuthorizedClients(@NonNull Client client) {
        return (scorm, cq, cb) -> {
            Join join = scorm.join("authorizedClients");

            cq.distinct(true);

            return cb.equal(join, client);
        };
    }
}
