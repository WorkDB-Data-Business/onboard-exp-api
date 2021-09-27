package br.com.harvest.onboardexperience.infra.storage.repositories;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.infra.storage.entities.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByContentPath(String contentPath);

    Page<File> findAllByAuthorizedClients(Client client, Pageable pageable);

    Optional<File> findByIdAndAuthorizedClients(Long id, Client client);

}
