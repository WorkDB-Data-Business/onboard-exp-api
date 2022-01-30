package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Event;
import br.com.harvest.onboardexperience.domain.entities.QuestionEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionEventRepository extends JpaRepository<QuestionEvent,Long> {


    Page<QuestionEvent> findByClient_Id(Long id, Pageable pageable);
}
