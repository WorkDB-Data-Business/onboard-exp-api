package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Reward;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long>{
	
	Optional<Reward> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<Reward> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);
	List<Reward> findAllByClient_Tenant(String tenant);

}
