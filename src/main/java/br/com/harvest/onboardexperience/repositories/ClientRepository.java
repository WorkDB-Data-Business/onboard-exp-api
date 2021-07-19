package br.com.harvest.onboardexperience.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

	Optional<Client> findByNameContainingIgnoreCase(String name);
	
	Optional<Client> findByTenantContainingIgnoreCase(String tenant);
	
	Optional<Client> findByCnpj(String cnpj);
	
	@Modifying
	@Query(value = "UPDATE tbuser SET is_active = ?1 FROM tbuser tu INNER JOIN tbclient tc ON tu.idclient = tc.idclient WHERE tc.idclient = ?2", nativeQuery = true)
	void disableAllUsersFromAClient(Boolean isActive, Long idClient);
	
	@Modifying
	@Query(value = "UPDATE tbuser SET is_expired = ?1 FROM tbuser tu INNER JOIN tbclient tc ON tu.idclient = tc.idclient WHERE tc.idclient = ?2", nativeQuery = true)
	void expireAllUsersFromAClient(Boolean isExpired, Long idClient);
	
	@Modifying
	@Query(value = "UPDATE tbuser SET is_blocked = ?1 FROM tbuser tu INNER JOIN tbclient tc ON tu.idclient = tc.idclient WHERE tc.idclient = ?2", nativeQuery = true)
	void blockAllUsersFromAClient(Boolean isBlocked, Long idClient);
	
}
