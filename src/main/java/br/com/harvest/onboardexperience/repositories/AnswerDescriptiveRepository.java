package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.AnswerDescriptive;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerDescriptiveRepository extends JpaRepository<AnswerDescriptive,Long>, JpaSpecificationExecutor<AnswerDescriptive> {

    static Specification<AnswerDescriptive> byIDQuestion(@NonNull Long idQuestion) {
        return (descriptive, cq, cb) ->
            cb.equal(descriptive.get("idQuestion").get("id"),idQuestion);
    }



}
