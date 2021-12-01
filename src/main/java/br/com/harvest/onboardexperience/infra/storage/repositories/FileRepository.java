package br.com.harvest.onboardexperience.infra.storage.repositories;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource
public interface FileRepository extends JpaRepository<HarvestFile, Long> {

    Optional<HarvestFile> findByContentPath(String contentPath);

    Page<HarvestFile> findAllByAuthorizedClients(Client client, Pageable pageable);

    Optional<HarvestFile> findByIdAndAuthorizedClients(Long id, Client client);

    Optional<HarvestFile> findByIdAndAuthor(Long id, User author);

}
