package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Questionnaire;
import br.com.harvest.onboardexperience.domain.entities.QuestionnaireMediaStage;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.entities.keys.QuestionnaireMediaStageId;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireMediaStageRepository extends JpaRepository<QuestionnaireMediaStage, QuestionnaireMediaStageId>,
        JpaSpecificationExecutor<QuestionnaireMediaStage> {

    Integer countByQuestionnaire(Questionnaire questionnaire);

    static Specification<QuestionnaireMediaStage> byStageAndQuestionnaire(@NonNull Stage stage, @NonNull Questionnaire questionnaire) {
        return (quizz, cq, cb) -> cb.and(
                cb.equal(quizz.get("questionnaire"), questionnaire),
                cb.equal(quizz.get("stage"), stage)
        );
    }

}
