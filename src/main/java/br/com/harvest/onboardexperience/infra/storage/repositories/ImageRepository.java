package br.com.harvest.onboardexperience.infra.storage.repositories;


import br.com.harvest.onboardexperience.infra.storage.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ImageRepository extends JpaRepository<Image, Long> {

    Boolean existsByFileName(String fileName);
}
