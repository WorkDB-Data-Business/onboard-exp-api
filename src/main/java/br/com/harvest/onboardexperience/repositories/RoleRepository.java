package br.com.harvest.onboardexperience.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Optional<Role> findByRole(RoleEnum role);
	
	@Query(value = "SELECT COUNT(*) FROM tbuser_role WHERE iduser = ?1", nativeQuery = true)
	Long getCountOfRolesFromUser(Long idUser);
	
	@Modifying
	@Query(value = "DELETE FROM tbuser_role WHERE iduser = ?1", nativeQuery = true)
	void deleteRelationshipFromUser(Long idUser);
	
}
