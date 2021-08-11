package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.harvest.onboardexperience.domain.entities.Coin;
public interface CoinRepository extends JpaRepository<Coin, Long>{
	
	Optional<Coin> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<Coin> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);
	List<Coin> findAllByClient_Tenant(String tenant);
	
}
