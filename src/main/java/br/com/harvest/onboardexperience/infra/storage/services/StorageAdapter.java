package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.infra.storage.dtos.LinkForm;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Component
public class StorageAdapter {

    @Autowired
    private ApplicationContext context;

    private StorageService storageService;

    private UploadForm form;

    private String token;

    public StorageAdapter setForm(LinkForm link, MultipartFile file, List<Long> authorizedClients, @NonNull String token){

        this.form = new UploadForm(file, link, authorizedClients);
        this.token = token;

        Storage storage = Objects.nonNull(form.getLink()) ? Storage.LINK : Storage.FILE;

        return setStorage(storage);
    }

    public StorageAdapter setStorage(@NonNull Storage storage){
        switch (storage) {
            case LINK:
                this.storageService = this.context.getBean(LinkStorageService.class);
                break;
            case FILE:
                this.storageService = this.context.getBean(FileStorageService.class);
        }
        return this;
    }

    public void save(){
        this.storageService.save(this.form, this.token);
    }

    public Page<?> findAll(@NonNull String token, Pageable pageable){
        return this.storageService.findAll(token, pageable);
    }
}
