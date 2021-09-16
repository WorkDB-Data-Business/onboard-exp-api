package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import br.com.harvest.onboardexperience.domain.entities.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.CompanyRole;

@Repository
public interface CompanyRoleRepository extends JpaRepository<CompanyRole, Long>{
	
	
	@Query(value = "UPDATE tbcompany_role SET is_active = false WHERE idclient = ?1", nativeQuery = true)
	@Modifying
	void disableAllByClient(Long idClient);
	
	Page<CompanyRole> findAllByClient_Tenant(String tenant, Pageable pageable);
	Optional<CompanyRole> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<CompanyRole> findByNameContainingIgnoreCase(String name);
	Optional<CompanyRole> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);
	Optional<CompanyRole> findByNameContainingIgnoreCaseOrIdAndClient_Tenant(String name, Long id, String tenant);

	@Query(value="SELECT c FROM CompanyRole c WHERE " +
			"c.client.tenant = :tenant AND " +
			"UPPER(c.name) LIKE '%'|| UPPER(:criteria) ||'%'")
	Page<CompanyRole> findByCriteria(String criteria, String tenant, Pageable pageable);
}
