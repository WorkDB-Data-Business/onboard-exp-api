package br.com.harvest.onboardexperience.infra.storage.services;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.FileMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileContentStore;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileRepository;
import br.com.harvest.onboardexperience.services.ClientService;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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
    private UserService userService;

    @Autowired
    private FetchService fetchService;

    private final Function<FileSimpleDto, FileSimpleDto> setStorage = fileSimpleDto -> {
        fileSimpleDto.setStorage(Storage.HARVEST_FILE);
        return fileSimpleDto;
    };

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
        return fileRepository.findAllByAuthorizedClients(client, pageable)
                .map(FileMapper.INSTANCE::toFileSimpleDto)
                .map(setStorage);
    }

    @Override
    public void update(@NonNull Long id, @NonNull UploadForm form, @NonNull String token) throws Exception {

        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, true);

        HarvestFile updatedHarvestFile = convertFormToFile(form, token);

        BeanUtils.copyProperties(updatedHarvestFile, harvestFile, "id", "author", "createdAt", "createdBy");

        uploadFile(harvestFile, form);

        fileRepository.save(harvestFile);
    }

    @Override
    public void delete(@NonNull Long id, @NonNull String token) throws Exception {
        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, true);

        fileContentStore.unsetContent(harvestFile);

        fileRepository.delete(harvestFile);
    }

    @Override
    public Optional<FileDto> find(@NonNull Long id, @NonNull String token) throws Exception {
        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, false);

        FileDto dto = FileMapper.INSTANCE.toDto(harvestFile);

        dto.setFileEncoded(StorageService.encodeFileToBase64(fileContentStore.getContent(harvestFile)));
        dto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(harvestFile.getAuthorizedClients(), Client.class));
        dto.setStorage(Storage.HARVEST_FILE);

        return Optional.of(dto);
    }

    @Override
    public void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull List<Long> authorizedClients) throws Exception {
        HarvestFile file = getFileByIdAndAuthorizedClient(id, token, true);

        file.setAuthorizedClients(fetchService.fetchClients(authorizedClients));

        fileRepository.save(file);
    }

    private HarvestFile getFileByIdAndAuthorizedClient(@NonNull Long id, @NonNull String token, Boolean validateAuthor) throws Exception {

        User user = userService.findUserByToken(token);

        HarvestFile harvestFile = fileRepository.findByIdAndAuthorizedClients(id, user.getClient())
                .or(() -> fileRepository.findByIdAndAuthor(id, user))
                .orElseThrow(
                () -> new FileNotFoundException("The requested file doesn't exist or you don't have access to get it."));

        if(validateAuthor){
            StorageService.validateAuthor(harvestFile, HarvestFile.class, user, "You're not the author of the file.");
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
        return MessageFormat.format("{0}/{1}/{2}", FILE_FOLDER, client.getCnpj(), file.getOriginalFilename());
    }

    private HarvestFile convertFormToFile(@NonNull UploadForm form, @NonNull String token) {
        User user = userService.findUserByToken(token);

        return HarvestFile.builder()
                .author(user)
                .description(form.getDescription())
                .authorizedClients(Objects.nonNull(form.getAuthorizedClients()) ? fetchService.fetchClients(form.getAuthorizedClients()) : null)
                .authorizedClients(generateAuthorizedClients(form.getAuthorizedClients(), user))
                .contentPath(createFilePath(form.getFile(), user.getClient()))
                .name(form.getFile().getOriginalFilename())
                .mimeType(form.getFile().getContentType()).build();
    }

    private List<Client> generateAuthorizedClients(List<Long> clientsId, @NonNull User user){
        if(ObjectUtils.isEmpty(clientsId)){
            clientsId = new ArrayList<>();
        }

        clientsId.add(user.getClient().getId());

        return fetchService.fetchClients(clientsId);
    }

}

