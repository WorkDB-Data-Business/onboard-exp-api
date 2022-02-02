package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Question;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.entities.Text;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextRepository extends JpaRepository<Text,Long> {

    Page<Text> findByClient_Id(Long id, Pageable pageable);
    Optional<Text> findByIdAndClient_Tenant(Long id, String tenant);
    Optional<Text> findByTitleAndClient_Tenant(String title, String tenant);



}
