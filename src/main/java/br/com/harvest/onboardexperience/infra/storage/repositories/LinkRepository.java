package br.com.harvest.onboardexperience.infra.storage.repositories;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Page<Link> findAllByAuthorizedClients(Client client, Pageable pageable);

    Optional<Link> findByIdAndAuthorizedClients(Long id, Client client);

    Optional<Link> findByIdAndAuthor(Long id, User author);

}
