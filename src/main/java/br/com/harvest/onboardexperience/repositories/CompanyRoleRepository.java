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
	
	
	@Query(value = "UPDATE tbcompany_role SET is_active = false WHERE idclient = ?1")
	@Modifying
	void disableAllByClient(Long idClient);
	
	@Query(value = "SELECT tbcompany_role.* FROM tbcompany_role, tbclient WHERE tbcompany_role.idclient = tbclient.idclient AND tbclient.tenant = ?1", 
			nativeQuery = true)
	List<CompanyRole> findAllByTenant(String tenant);
	
	@Query(value = "SELECT tbcompany_role.* FROM tbcompany_role, tbclient WHERE tbcompany_role.idclient = tbclient.idclient AND tbcompany_role.idcompany_role = ?1 "
			+ "AND tbclient.tenant = ?2", 
			nativeQuery = true)
	Optional<CompanyRole> findByIdAndTenant(Long id, String tenant);
	
	Optional<CompanyRole> findByNameContainingIgnoreCase(String name);
	
	@Query(value = "SELECT tbcrole.* FROM tbcompany_role tbcrole, tbclient tbc WHERE tbcrole.idclient = tbc.idclient"
			+ " AND UPPER(tbcrole.name) = UPPER(?1)"
			+ " AND tbc.tenant = ?2", nativeQuery = true)
	Optional<CompanyRole> findByNameContainingIgnoreCaseAndTenant(String name, String tenant);

}
