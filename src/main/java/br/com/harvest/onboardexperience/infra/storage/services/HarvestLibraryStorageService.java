package br.com.harvest.onboardexperience.infra.storage.services;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.FileSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.filters.HarvestLibraryFilter;
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
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    @Autowired
    private ImageStorageService imageStorageService;

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
    public Page<FileSimpleDto> findAll(@NonNull String token, HarvestLibraryFilter filter, Pageable pageable) {
        return fileRepository.findAll(createQuery(filter, token), pageable)
                .map(FileMapper.INSTANCE::toFileSimpleDto)
                .map(setStorage);
    }

    private Specification<HarvestFile> createQuery(@NonNull HarvestLibraryFilter filter, @NonNull String token){
        Specification<HarvestFile> query = Specification.where(
                FileRepository.byAuthorizedClients(tenantService.fetchClientByTenantFromToken(token)));

        if(StringUtils.hasText(filter.getCriteriaFilter())) {
            query = query.and(FileRepository.byCustomFilter(filter.getCriteriaFilter()));
        }

        return query;
    }

    @Override
    public void update(@NonNull String id, @NonNull UploadForm form, @NonNull String token) throws Exception {

        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, true);

        HarvestFile updatedHarvestFile = convertFormToFile(form, token);

        BeanUtils.copyProperties(updatedHarvestFile, harvestFile, "id", "author", "createdAt", "createdBy");

        uploadFile(harvestFile, form);

        fileRepository.save(harvestFile);
    }

    @Override
    public void delete(@NonNull String id, @NonNull String token) throws Exception {
        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, true);

        fileContentStore.unsetContent(harvestFile);

        fileRepository.delete(harvestFile);
    }

    @Override
    public Optional<FileDto> find(@NonNull String id, @NonNull String token) throws Exception {
        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, false);

        FileDto dto = FileMapper.INSTANCE.toDto(harvestFile);

        dto.setFileEncoded(StorageService.encodeFileToBase64(fileContentStore.getContent(harvestFile)));
        dto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(harvestFile.getAuthorizedClients(), Client.class));
        dto.setStorage(Storage.HARVEST_FILE);

        return Optional.of(dto);
    }

    @Override
    public void updateAuthorizedClients(@NonNull String id, @NonNull String token, @NonNull List<Long> authorizedClients) throws Exception {
        HarvestFile file = getFileByIdAndAuthorizedClient(id, token, true);

        file.setAuthorizedClients(fetchService.fetchClients(authorizedClients));

        fileRepository.save(file);
    }

    private HarvestFile getFileByIdAndAuthorizedClient(@NonNull String id, @NonNull String token, Boolean validateAuthor) throws Exception {

        User user = userService.findUserByToken(token);

        HarvestFile harvestFile = fileRepository.findOne(FileRepository.byIdAsString(id).and(FileRepository.byAuthorizedClients(user.getClient())))
                .or(() -> fileRepository.findOne(FileRepository.byIdAsString(id).and(FileRepository.byAuthor(user))))
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
            harvestFile.setPreviewImagePath(
                    imageStorageService.uploadImage(form.getPreviewImage(),
                            harvestFile.getAuthor().getClient().getCnpj(),
                            harvestFile.getName() + "_preview",
                            FileTypeEnum.IMAGE, harvestFile.getAuthor()));
        } catch (IOException e) {
            log.error("An error occurs while uploading the file", e);
            throw new GenericUploadException(e.getMessage(), e.getCause());
        }
    }

    private String createFilePath(String fileName, Client client) {
        return MessageFormat.format("{0}/{1}/{2}", FILE_FOLDER, client.getCnpj(), fileName);
    }

    private HarvestFile convertFormToFile(@NonNull UploadForm form, @NonNull String token) {
        User user = userService.findUserByToken(token);
        String fileName = form.getName() + "." + FilenameUtils.getExtension(form.getFile().getOriginalFilename());

        return HarvestFile.builder()
                .author(user)
                .name(form.getName())
                .description(form.getDescription())
                .authorizedClients(generateAuthorizedClients(form.getAuthorizedClients(), user))
                .fileName(fileName)
                .contentPath(createFilePath(fileName, user.getClient()))
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

