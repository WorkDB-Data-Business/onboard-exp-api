package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Reward;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageRepository extends JpaRepository<Stage,Long> {

    Optional<Stage> findByIdAndClient_Tenant(Long id, String tenant);
    Optional<Stage> findByIdAndClient(Long id, Client client);
    Optional<Stage> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);
    Optional<Stage> findByNameAndClient_Tenant(String name, String tenant);
    Optional<Stage> findByNameAndClient(String name, Client client);

    List<Stage> findAllByIsAvailableAndClient_Tenant(Boolean isAvailable, String tenant);

    Page<Stage> findAllByClient_Tenant(String tenant, Pageable pageable);

    @Query(value="SELECT r FROM Stage r WHERE " +
            "r.client.tenant = :tenant AND (" +
            "UPPER(r.name) LIKE '%'|| UPPER(:criteria) ||'%' OR " +
            "UPPER(r.description) LIKE '%'|| UPPER(:criteria) ||'%')")
    Page<Stage> findByCriteria(String criteria, String tenant, Pageable pageable);

}
