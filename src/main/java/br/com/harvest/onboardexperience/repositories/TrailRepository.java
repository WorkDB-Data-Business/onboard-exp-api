package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailRepository extends JpaRepository<Trail,Long> {
}
