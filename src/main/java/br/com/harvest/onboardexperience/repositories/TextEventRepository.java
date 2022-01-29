package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.QuestionEvent;
import br.com.harvest.onboardexperience.domain.entities.TextEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextEventRepository extends JpaRepository<TextEvent,Long> {



}
