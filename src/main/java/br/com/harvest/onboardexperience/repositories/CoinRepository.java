package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import br.com.harvest.onboardexperience.domain.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.harvest.onboardexperience.domain.entities.Coin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
	
	Optional<Coin> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<Coin> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);
	Optional<Coin> findByNameAndClient_Tenant(String name, String tenant);
	Page<Coin> findAllByClient_Tenant(String tenant, Pageable pageable);
	List<Coin> findAllByClient(Client client);

	@Query(value="SELECT c FROM Coin c WHERE " +
			"c.client.tenant = :tenant AND " +
			"UPPER(c.name) LIKE '%'|| UPPER(:criteria) ||'%'")
	Page<Coin> findByCriteria(String criteria, String tenant, Pageable pageable);
	
}
