package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.infra.storage.dtos.LinkForm;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.filters.HarvestLibraryFilter;
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

    public StorageAdapter setForm(LinkForm link, MultipartFile file, MultipartFile previewFile, List<Long> authorizedClients,
                                  String description, String name, Storage storage, @NonNull String token){

        this.form = new UploadForm(file, previewFile, link, authorizedClients, description, name);
        this.token = token;

        return setStorage(storage);
    }

    public StorageAdapter setStorage(@NonNull Storage storage){
        switch (storage) {
            case LINK:
                this.storageService = this.context.getBean(LinkStorageService.class);
                break;
            case HARVEST_FILE:
                this.storageService = this.context.getBean(HarvestFileStorageService.class);
                break;
            case SCORM:
                this.storageService = this.context.getBean(ScormStorageService.class);
                break;
            default:
                this.storageService = null;
        }
        return this;
    }

    public void save() throws Exception {
        this.storageService.save(this.form, this.token);
    }

    public Page<?> findAll(@NonNull String token, HarvestLibraryFilter filter, Pageable pageable){
        validate();
        return this.storageService.findAll(token, filter, pageable);
    }

    public void update(@NonNull String id) throws Exception {
        validate();
        this.storageService.update(id, this.form, this.token);
    }

    public void delete(@NonNull String id, @NonNull String token) throws Exception {
        validate();
        this.storageService.delete(id, token);
    }

    public void updateAuthorizedClients(@NonNull String id, @NonNull String token, @NonNull  List<Long> authorizedClients) throws Exception {
        validate();
        this.storageService.updateAuthorizedClients(id, token, authorizedClients);
    }

    public Optional<?> find(@NonNull String id, @NonNull String token) throws Exception {
        validate();
        return  this.storageService.find(id, token);
    }

    private void validate(){
        if(Objects.isNull(this.storageService)){
            throw new NullPointerException("The storage service can't be null.");
        }
    }
}
