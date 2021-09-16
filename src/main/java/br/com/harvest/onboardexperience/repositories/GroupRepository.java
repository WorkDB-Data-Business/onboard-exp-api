package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.Group;
import br.com.harvest.onboardexperience.domain.enumerators.GroupType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Page<Group> findAllByClient_Tenant(String tenant, Pageable pageable);

    Optional<Group> findByIdAndClient_Tenant(Long id, String tenant);

    Optional<Group> findByNameContainingIgnoreCaseAndClient_Tenant(String name, String tenant);

}
