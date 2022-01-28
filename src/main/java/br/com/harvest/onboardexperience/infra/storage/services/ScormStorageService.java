package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ScormStorageService implements StorageService {


    @Override
    public void save(@NonNull UploadForm form, @NonNull String token) {

    }

    @Override
    public void validate(@NonNull UploadForm form) {

    }

    @Override
    public Page<?> findAll(@NonNull String token, Pageable pageable) {
        return null;
    }

    @Override
    public void update(@NonNull Long id, @NonNull UploadForm form, @NonNull String token) throws Exception {

    }

    @Override
    public void delete(@NonNull Long id, @NonNull String token) throws Exception {

    }

    @Override
    public Optional<?> find(@NonNull Long id, @NonNull String token) throws Exception {
        return Optional.empty();
    }

    @Override
    public void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull List<Long> authorizedClients) throws Exception {

    }
}
