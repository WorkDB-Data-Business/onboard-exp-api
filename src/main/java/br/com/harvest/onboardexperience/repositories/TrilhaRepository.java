package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Trilha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrilhaRepository extends JpaRepository<Trilha,Long> {
}
