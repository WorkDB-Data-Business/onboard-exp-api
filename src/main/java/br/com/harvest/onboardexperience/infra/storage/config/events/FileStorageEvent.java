package br.com.harvest.onboardexperience.infra.storage.config.events;

import br.com.harvest.onboardexperience.infra.storage.entities.File;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.content.commons.annotations.HandleBeforeSetContent;
import org.springframework.content.commons.annotations.StoreEventHandler;

import java.nio.file.FileAlreadyExistsException;


@StoreEventHandler
public class FileStorageEvent {

    @Autowired
    private FileRepository fileRepository;

    @HandleBeforeSetContent
    public void handleBeforeSetContent(File file) throws FileAlreadyExistsException {
        if(fileRepository.findByContentPath(file.getContentPath()).isPresent()){
            throw new FileAlreadyExistsException(file.getName() + " already exists.");
        }
    }

}
