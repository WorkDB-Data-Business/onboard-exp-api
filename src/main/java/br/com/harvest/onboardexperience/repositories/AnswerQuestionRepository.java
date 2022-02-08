package br.com.harvest.onboardexperience.repositories;


import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerQuestionRepository extends JpaRepository<AnswerQuestion,Long> {


}
