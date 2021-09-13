package br.com.harvest.onboardexperience.repositories;

import java.util.List;
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
	
	@Query(value = "SELECT c FROM Client c WHERE c.isActive = false")
	List<Client> findAllInactiveClients();
	
	@Query(value = "SELECT c FROM Client c WHERE c.isBlocked = true")
	List<Client> findAllBlockedClients();
	
	@Query(value = "SELECT c FROM Client c WHERE c.isExpired = true")
	List<Client> findAllExpiredClients();
	
	@Modifying
	@Query(value = "UPDATE tbuser SET is_active = ?1 FROM tbclient WHERE tbuser.idclient = tbclient.idclient AND tbclient.idclient = ?2", nativeQuery = true)
	void disableAllUsersFromAClient(Boolean isActive, Long idClient);
	
	@Modifying
	@Query(value = "UPDATE tbuser SET is_expired = ?1 FROM tbclient WHERE tbuser.idclient = tbclient.idclient AND tbclient.idclient = ?2", nativeQuery = true)
	void expireAllUsersFromAClient(Boolean isExpired, Long idClient);
	
	@Modifying
	@Query(value = "UPDATE tbuser SET is_blocked = ?1 FROM tbclient WHERE tbuser.idclient = tbclient.idclient AND tbclient.idclient = ?2", nativeQuery = true)
	void blockAllUsersFromAClient(Boolean isBlocked, Long idClient);
	
}
