package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.QuestionnaireMediaStage;
import br.com.harvest.onboardexperience.domain.entities.QuestionnaireMediaUser;
import br.com.harvest.onboardexperience.domain.entities.keys.QuestionnaireMediaUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireMediaUserRepository extends JpaRepository<QuestionnaireMediaUser, QuestionnaireMediaUserId> {

    Boolean existsByQuestionnaireMedia(QuestionnaireMediaStage questionnaireMediaStage);

}
