package br.com.harvest.onboardexperience.infra.storage.config.events;

import br.com.harvest.onboardexperience.infra.storage.entities.Image;
import br.com.harvest.onboardexperience.infra.storage.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.content.commons.annotations.HandleBeforeSetContent;
import org.springframework.content.commons.annotations.StoreEventHandler;

import java.nio.file.FileAlreadyExistsException;

@StoreEventHandler
public class ImageStorageEvent {

    @Autowired
    private ImageRepository repository;

    @HandleBeforeSetContent
    public void handleBeforeSetContent(Image image) throws FileAlreadyExistsException {
        if(repository.existsByFileName(image.getFileName())){
            throw new FileAlreadyExistsException(image.getFileName() + " already exists.");
        }
    }

}
