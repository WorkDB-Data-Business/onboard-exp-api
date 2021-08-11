package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

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
	
	List<CompanyRole> findAllByClient_Tenant(String tenant);
	Optional<CompanyRole> findByIdAndClient_Tenant(Long id, String tenant);
	Optional<CompanyRole> findByNameContainingIgnoreCase(String name);
	Optional<CompanyRole> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);

}
