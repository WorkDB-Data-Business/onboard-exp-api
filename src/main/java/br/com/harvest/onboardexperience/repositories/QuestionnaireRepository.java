package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Questionnaire;
import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;


@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long>, JpaSpecificationExecutor<Questionnaire> {

    static Specification<Questionnaire> byAuthor(@NonNull User author) {
        return (questionnaire, cq, cb) -> cb.equal(questionnaire.get("author"), author);
    }

    static Specification<Questionnaire> byId(@NonNull Long id) {
        return (questionnaire, cq, cb) -> cb.equal(questionnaire.get("id"), id);
    }

    static Specification<Questionnaire> byAuthorizedClients(@NonNull Client client) {
        return (questionnaire, cq, cb) -> {
            Join join = questionnaire.join("authorizedClients");

            cq.distinct(true);

            return cb.equal(join, client);
        };
    }

}
