package br.com.harvest.onboardexperience.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Permission;
import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
	
	Optional<Permission> findByPermission(PermissionEnum permission);

}
