package br.com.harvest.onboardexperience.repositories;


import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import br.com.harvest.onboardexperience.domain.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerQuestionRepository extends JpaRepository<AnswerQuestion,Long> {


}
