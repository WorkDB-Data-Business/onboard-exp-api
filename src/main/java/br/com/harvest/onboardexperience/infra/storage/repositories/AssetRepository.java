package br.com.harvest.onboardexperience.infra.storage.repositories;


import br.com.harvest.onboardexperience.infra.storage.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Boolean existsByFileName(String fileName);
}
