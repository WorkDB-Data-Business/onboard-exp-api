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
import java.util.Optional;

@Component
public class StorageAdapter {

    @Autowired
    private ApplicationContext context;

    private StorageService storageService;

    private UploadForm form;

    private String token;

    public StorageAdapter setForm(LinkForm link, MultipartFile file, List<Long> authorizedClients, String description, @NonNull String token){

        this.form = new UploadForm(file, link, authorizedClients, description);
        this.token = token;

        Storage storage = Objects.nonNull(form.getLink()) ? Storage.LINK : Storage.HARVEST_FILE;

        return setStorage(storage);
    }

    public StorageAdapter setStorage(@NonNull Storage storage){
        switch (storage) {
            case LINK:
                this.storageService = this.context.getBean(LinkStorageService.class);
                break;
            case HARVEST_FILE:
                this.storageService = this.context.getBean(HarvestLibraryStorageService.class);
                break;
            default:
                this.storageService = null;
        }
        return this;
    }
<<<<<<< HEAD
        public void save(){
=======

    public void save(){
>>>>>>> c896888945cca12305fdf6c50f9a12fb5f344d7e
        validate();
        this.storageService.save(this.form, this.token);
    }

    public Page<?> findAll(@NonNull String token, Pageable pageable){
        validate();
        return this.storageService.findAll(token, pageable);
    }

    public void update(@NonNull Long id) throws Exception {
        validate();
        this.storageService.update(id, this.form, this.token);
    }

    public void delete(@NonNull Long id, @NonNull String token) throws Exception {
        validate();
        this.storageService.delete(id, token);
    }

    public void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull  List<Long> authorizedClients) throws Exception {
        validate();
        this.storageService.updateAuthorizedClients(id, token, authorizedClients);
    }

    public Optional<?> find(@NonNull Long id, @NonNull String token) throws Exception {
        validate();
        return  this.storageService.find(id, token);
    }

    private void validate(){
        if(Objects.isNull(this.storageService)){
            throw new NullPointerException("The storage service can't be null.");
        }
    }
}
