package br.com.harvest.onboardexperience.infra.storage.services;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.domain.exceptions.LinkNotFoundException;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.FileMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileContentStore;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileRepository;
import br.com.harvest.onboardexperience.services.ClientService;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class HarvestLibraryStorageService implements StorageService {

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

        HarvestFile harvestFile = convertFormToFile(form, token);

        uploadFile(harvestFile, form);

        fileRepository.save(harvestFile);
    }

    @Override
    public void validate(UploadForm form) {
        if (Objects.isNull(form.getFile())) {
            throw new NullPointerException("The file cannot be null.");
        }
    }

    @Override
    public Page<FileSimpleDto> findAll(@NonNull String token, Pageable pageable) {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return fileRepository.findAllByAuthorizedClients(client, pageable).map(FileMapper.INSTANCE::toFileSimpleDto);
    }

    @Override
    public void update(@NonNull Long id, @NonNull UploadForm form, @NonNull String token) {

        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, true);

        HarvestFile updatedHarvestFile = convertFormToFile(form, token);

        BeanUtils.copyProperties(updatedHarvestFile, harvestFile, "id", "createdAt", "createdBy");

        uploadFile(harvestFile, form);

        fileRepository.save(harvestFile);
    }

    @Override
    public void delete(@NonNull Long id, @NonNull String token) {
        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, true);

        fileContentStore.unsetContent(harvestFile);

        fileRepository.delete(harvestFile);
    }

    @Override
    public Optional<FileDto> find(@NonNull Long id, @NonNull String token) {
        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, false);

        FileDto dto = FileMapper.INSTANCE.toDto(harvestFile);

        dto.setFileEncoded(encodeFileToBase64(fileContentStore.getContent(harvestFile)));
        dto.setAuthorizedClientsId(StorageService.getIDFromClients(harvestFile.getAuthorizedClients()));

        return Optional.of(dto);
    }

    @Override
    public void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull List<Long> authorizedClients) {
        HarvestFile file = getFileByIdAndAuthorizedClient(id, token, true);

        file.setAuthorizedClients(fetchService.fetchClients(authorizedClients,
                tenantService.fetchClientByTenantFromToken(token)));

        fileRepository.save(file);
    }

    private String encodeFileToBase64(InputStream inputStream){
        try {
            return Base64.encodeBase64String(IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HarvestFile getFileByIdAndAuthorizedClient(@NonNull Long id, @NonNull String token, @NonNull Boolean validateAuthor) {

        Client client = tenantService.fetchClientByTenantFromToken(token);

        HarvestFile harvestFile = fileRepository.findByIdAndAuthorizedClients(id, client).orElseThrow(
                () -> new LinkNotFoundException("File not found.", "The requested file doesn't exist or you don't have access to get it.")
        );

        if(validateAuthor){
            StorageService.validateAuthor(client, harvestFile.getAuthorizedClients());
        }

        return harvestFile;
    }

    private void uploadFile(@NonNull HarvestFile harvestFile, @NonNull UploadForm form) {
        try {
            fileContentStore.setContent(harvestFile, form.getFile().getInputStream());
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

    private HarvestFile convertFormToFile(@NonNull UploadForm form, @NonNull String token) {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return HarvestFile.builder()
                .authorizedClients(fetchService.fetchClients(form.getAuthorizedClients(), client))
                .contentPath(createFilePath(form.getFile(), client))
                .name(form.getFile().getOriginalFilename())
                .mimeType(form.getFile().getContentType()).build();
    }

}
