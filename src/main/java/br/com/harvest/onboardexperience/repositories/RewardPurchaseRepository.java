package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.RewardPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardPurchaseRepository extends JpaRepository<RewardPurchase, Long> {

}
