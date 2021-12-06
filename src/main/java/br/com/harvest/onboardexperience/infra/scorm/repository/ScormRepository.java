package br.com.harvest.onboardexperience.infra.scorm.repository;

import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScormRepository extends JpaRepository<Scorm, String> {

}
