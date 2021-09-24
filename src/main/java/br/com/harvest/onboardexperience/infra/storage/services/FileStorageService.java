package br.com.harvest.onboardexperience.infra.storage.services;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.File;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.FileMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileContentStore;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileRepository;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.services.ClientService;
import br.com.harvest.onboardexperience.services.TenantService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final String FILE_FOLDER = "files";

    public void save(@NonNull UploadForm form, String token) {

        validate(form);

        Client client = tenantService.fetchClientByTenantFromToken(token);

        File file = File.builder()
                .authorizedClients(fetchClients(form.getAuthorizedClients(), client))
                .contentPath(createFilePath(form.getFile(), client))
                .name(form.getFile().getOriginalFilename())
                .mimeType(form.getFile().getContentType()).build();

        try {
            fileContentStore.setContent(file, form.getFile().getInputStream());
        } catch (IOException e){
            log.error("An error occurs while uploading the file", e);
            throw new GenericUploadException(e.getMessage(), e.getCause());
        }

        fileRepository.save(file);

    }

    @Override
    public void validate(UploadForm form) {
        if(Objects.isNull(form.getFile())){
            throw new NullPointerException("The file cannot be null.");
        }
    }

    @Override
    public Page<?> findAll(@NonNull String token, Pageable pageable) {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return fileRepository.findAllByAuthorizedClients(client, pageable).map(FileMapper.INSTANCE::toFileSimpleDto);
    }

    private String createFilePath(MultipartFile file, Client client){
        StringBuilder builder = new StringBuilder(FILE_FOLDER)
                .append("/")
                .append(client.getCnpj())
                .append("/")
                .append(file.getOriginalFilename());
        return builder.toString();
    }

    private List<Client> fetchClients(List<Long> clientsId, Client author){
        List<Client> clients = new ArrayList<>() {{ add(author); }};

        if(ObjectUtils.isNotEmpty(clientsId)){
            for(Long clientId : clientsId){
                if(clientId.equals(author.getId())) continue;
                Client client = ClientMapper.INSTANCE.toEntity(clientService.findById(clientId));
                clients.add(client);
            }
        }

        return clients;
    }

}

