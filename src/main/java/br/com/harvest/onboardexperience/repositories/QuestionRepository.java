package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Question;
import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Join;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long>, JpaSpecificationExecutor<Question> {

    static Specification<Question> byAuthorizedClients(@NonNull Client client) {
        return (question, cq, cb) -> {
            Join join = question.join("authorizedClients");

            cq.distinct(true);

            return cb.equal(join, client);
        };
    }

    static Specification<Question> byAuthor(@NonNull User author) {
        return (question, cq, cb) -> cb.equal(question.get("author"), author);
    }

    static Specification<Question> byId(@NonNull Long id) {
        return (question, cq, cb) -> cb.equal(question.get("id"), id);
    }


}
