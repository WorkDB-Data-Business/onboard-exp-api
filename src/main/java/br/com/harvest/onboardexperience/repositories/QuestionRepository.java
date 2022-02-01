package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {


    Page<Question> findByClient_Id(Long id, Pageable pageable);

    Optional<Question> findByIdAndClient_Tenant(Long id, String tenant);


}
