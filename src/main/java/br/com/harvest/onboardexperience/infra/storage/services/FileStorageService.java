package br.com.harvest.onboardexperience.infra.storage.services;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.domain.exceptions.LinkNotFoundException;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.File;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.FileMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileContentStore;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileRepository;
import br.com.harvest.onboardexperience.services.ClientService;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class FileStorageService implements StorageService {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private FileContentStore fileContentStore;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private FetchService fetchService;

    private final String FILE_FOLDER = "files";

    public void save(@NonNull UploadForm form, @NonNull String token) {

        validate(form);

        File file = convertFormToFile(form, token);

        uploadFile(file, form);

        fileRepository.save(file);

    }

    @Override
    public void validate(UploadForm form) {
        if (Objects.isNull(form.getFile())) {
            throw new NullPointerException("The file cannot be null.");
        }
    }

    @Override
    public Page<?> findAll(@NonNull String token, Pageable pageable) {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return fileRepository.findAllByAuthorizedClients(client, pageable).map(FileMapper.INSTANCE::toFileSimpleDto);
    }

    @Override
    public void update(@NonNull Long id, @NonNull UploadForm form, @NonNull String token) {

        File file = getFileByIdAndAuthorizedClient(id, token, true);

        File updatedFile = convertFormToFile(form, token);

        BeanUtils.copyProperties(updatedFile, file, "id", "createdAt", "createdBy");

        uploadFile(file, form);

        fileRepository.save(file);
    }

    @Override
    public void delete(@NonNull Long id, @NonNull String token) {
        File file = getFileByIdAndAuthorizedClient(id, token, true);

        fileContentStore.unsetContent(file);

        fileRepository.delete(file);
    }

    @Override
    public Optional<?> find(@NonNull Long id, @NonNull String token) {
        File file = getFileByIdAndAuthorizedClient(id, token, true);

        //TODO: continuar implementação.
        return Optional.empty();
    }

    private File getFileByIdAndAuthorizedClient(@NonNull Long id, @NonNull String token, @NonNull Boolean validateAuthor) {

        Client client = tenantService.fetchClientByTenantFromToken(token);

        File file = fileRepository.findByIdAndAuthorizedClients(id, client).orElseThrow(
                () -> new LinkNotFoundException("File not found.", "The requested file doesn't exist or you don't have access to get it.")
        );

        if(validateAuthor){
            StorageService.validateAuthor(client, file.getAuthorizedClients());
        }

        return file;
    }

    private void uploadFile(@NonNull File file, @NonNull UploadForm form) {
        try {
            fileContentStore.setContent(file, form.getFile().getInputStream());
        } catch (IOException e) {
            log.error("An error occurs while uploading the file", e);
            throw new GenericUploadException(e.getMessage(), e.getCause());
        }
    }

    private String createFilePath(MultipartFile file, Client client) {
        StringBuilder builder = new StringBuilder(FILE_FOLDER)
                .append("/")
                .append(client.getCnpj())
                .append("/")
                .append(file.getOriginalFilename());
        return builder.toString();
    }

    private File convertFormToFile(@NonNull UploadForm form, @NonNull String token) {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return File.builder()
                .authorizedClients(fetchService.fetchClients(form.getAuthorizedClients(), client))
                .contentPath(createFilePath(form.getFile(), client))
                .name(form.getFile().getOriginalFilename())
                .mimeType(form.getFile().getContentType()).build();
    }

}

