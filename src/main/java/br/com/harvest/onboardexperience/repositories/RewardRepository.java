package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import br.com.harvest.onboardexperience.domain.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Reward;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long>{
	
	Optional<Reward> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<Reward> findByIdAndClient(Long id, Client client);
	Optional<Reward> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);
	Optional<Reward> findByNameAndClient_Tenant(String name, String tenant);
	Optional<Reward> findByNameAndClient(String name, Client client);

	Page<Reward> findAllByClient_Tenant(String tenant, Pageable pageable);
	List<Reward> findAllByClient(Client client);

	@Query(value="SELECT r FROM Reward r WHERE " +
			"r.client.tenant = :tenant AND (" +
			"UPPER(r.name) LIKE '%'|| UPPER(:criteria) ||'%' OR " +
			"r.price||'' LIKE '%'|| UPPER(:criteria) ||'%' OR " +
			"UPPER(r.description) LIKE '%'|| UPPER(:criteria) ||'%')")
	Page<Reward> findByCriteria(String criteria, String tenant, Pageable pageable);

}
