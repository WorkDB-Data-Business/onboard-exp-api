package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

    Optional<Event> findByIdAndClient_Tenant(Long id, String tenant);
    Optional<Event> findByIdAndClient(Long id, Client client);
    Optional<Event> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);
    Optional<Event> findByNameAndClient_Tenant(String name, String tenant);
    Optional<Event> findByNameAndClient(String name, Client client);


    Page<Event> findAllByClient_Tenant(String tenant, Pageable pageable);

    @Query(value="SELECT r FROM Event r WHERE " +
            "r.client.tenant = :tenant AND (" +
            "UPPER(r.name) LIKE '%'|| UPPER(:criteria) ||'%' OR " +
            "UPPER(r.description) LIKE '%'|| UPPER(:criteria) ||'%')")
    Page<Event> findByCriteria(String criteria, String tenant, Pageable pageable);
}
