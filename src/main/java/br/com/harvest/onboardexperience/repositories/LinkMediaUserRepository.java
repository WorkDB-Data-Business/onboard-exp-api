package br.com.harvest.onboardexperience.repositories;

import br.com.harvest.onboardexperience.domain.entities.LinkMediaUser;
import br.com.harvest.onboardexperience.domain.entities.keys.LinkMediaUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkMediaUserRepository extends JpaRepository<LinkMediaUser, LinkMediaUserId> {

}
