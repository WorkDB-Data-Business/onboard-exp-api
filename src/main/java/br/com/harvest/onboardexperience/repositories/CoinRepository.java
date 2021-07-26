package br.com.harvest.onboardexperience.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.harvest.onboardexperience.domain.entities.Coin;
public interface CoinRepository extends JpaRepository<Coin, Long>{
	
	@Query(value = "SELECT tbcoin.* FROM tbcoin, tbclient WHERE tbcoin.idclient = tbclient.idclient AND tbcoin.idcoin = ?1"
			+ " AND tbclient.tenant = ?2", 
			nativeQuery = true)
	Optional<Coin> findByIdAndTenant(Long id, String tenant);
	
	@Query(value = "SELECT tbcoin.* FROM tbcoin, tbclient WHERE tbcoin.idclient = tbclient.idclient AND UPPER(tbcoin.name) = UPPER(?1)"
			+ " AND tbclient.tenant = ?2", 
			nativeQuery = true)
	Optional<Coin> findByNameContainingIgnoreCaseAndTenant(String name, String tenant);
	
	@Query(value = "SELECT tbcoin.* FROM tbcoin, tbclient WHERE tbcoin.idclient = tbclient.idclient AND tbclient.tenant = ?1", 
			nativeQuery = true)
	List<Coin> findAllByTenant(String tenant);
	
}
