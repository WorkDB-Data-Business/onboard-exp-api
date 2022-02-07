package br.com.harvest.onboardexperience.infra.storage.config.events;

import br.com.harvest.onboardexperience.infra.storage.entities.Asset;
import br.com.harvest.onboardexperience.infra.storage.repositories.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.content.commons.annotations.HandleBeforeSetContent;
import org.springframework.content.commons.annotations.StoreEventHandler;

import java.nio.file.FileAlreadyExistsException;

@StoreEventHandler
public class ImageStorageEvent {

    @Autowired
    private AssetRepository repository;

    @HandleBeforeSetContent
    public void handleBeforeSetContent(Asset asset) throws FileAlreadyExistsException {
        if(repository.existsByFileName(asset.getFileName())){
            throw new FileAlreadyExistsException(asset.getFileName() + " already exists.");
        }
    }

}
