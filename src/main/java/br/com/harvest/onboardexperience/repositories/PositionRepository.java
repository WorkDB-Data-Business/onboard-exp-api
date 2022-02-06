package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Position;
import br.com.harvest.onboardexperience.domain.entities.keys.PositionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, PositionId> {

}
