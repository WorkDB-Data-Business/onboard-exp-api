package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.harvest.onboardexperience.domain.entities.Reward;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long>{
	
	@Query(value = "SELECT tbreward.* FROM tbreward, tbclient WHERE tbreward.idclient = tbclient.idclient AND tbreward.idcoin = ?1"
			+ " AND tbclient.tenant = ?2", 
			nativeQuery = true)
	Optional<Reward> findByIdAndTenant(Long id, String tenant);
	
	@Query(value = "SELECT tbreward.* FROM tbreward, tbclient WHERE tbreward.idclient = tbclient.idclient AND UPPER(tbreward.name) = UPPER(?1)"
			+ " AND tbclient.tenant = ?2", 
			nativeQuery = true)
	Optional<Reward> findByNameContainingIgnoreCaseAndTenant(String name, String tenant);
	
	@Query(value = "SELECT tbreward.* FROM tbreward, tbclient WHERE tbreward.idclient = tbclient.idclient AND tbclient.tenant = ?1", 
			nativeQuery = true)
	List<Reward> findAllByTenant(String tenant);

}
