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
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class HarvestFileStorageService implements StorageService {

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
    private AssetStorageService assetStorageService;

    @Autowired
    private FileStorageService fileStorageService;

    private final Function<FileSimpleDto, FileSimpleDto> setStorage = fileSimpleDto -> {
        fileSimpleDto.setStorage(Storage.HARVEST_FILE);
        return fileSimpleDto;
    };

    private final String FILE_FOLDER = "files";

    public void save(@NonNull UploadForm form, @NonNull String token) throws FileAlreadyExistsException {

        validate(form);


        HarvestFile harvestFile = convertFormToFile(form, token);

        validateIfAlreadyExists(harvestFile);

        uploadFile(harvestFile, form);
        uploadPreviewImage(harvestFile, form);

        fileRepository.save(harvestFile);
    }

    @Override
    public void validate(UploadForm form) {
        if (Objects.isNull(form.getFile())) {
            throw new GenericUploadException("The file cannot be null.");
        }

        if(Objects.isNull(form.getPreviewImage())){
            throw new GenericUploadException("The preview image cannot be null.");
        }
    }

    @Override
    public Page<FileSimpleDto> findAll(@NonNull String token, HarvestLibraryFilter filter, Pageable pageable) {
        return fileRepository.findAll(createQuery(filter, token), pageable)
                .map(this::toFileSimpleDto)
                .map(setStorage);
    }

    private FileSimpleDto toFileSimpleDto(@NonNull HarvestFile harvestFile) {
        FileSimpleDto fileSimpleDto = FileMapper.INSTANCE.toFileSimpleDto(harvestFile);
        try {
            FileDto fileDto = fileStorageService.find(harvestFile.getPreviewImagePath());
            fileSimpleDto.setImagePreviewEncoded(Objects.nonNull(fileDto) ? fileDto.getFileEncoded() : null);
        } catch (FileNotFoundException e) {
            log.info("An error occurred while getting image preview file:", e);
        }

        return fileSimpleDto;
    }

    public List<HarvestFile> findAllByClient(@NonNull Client client) {
        return fileRepository.findAll(FileRepository.byClient(client));
    }

    private Specification<HarvestFile> createQuery(@NonNull HarvestLibraryFilter filter, @NonNull String token){
        Specification<HarvestFile> query = Specification.where(
                FileRepository.byAuthorizedClients(tenantService.fetchClientByTenantFromToken(token)))
                .and(FileRepository.byIsNotAsset());

        if(StringUtils.hasText(filter.getCustomFilter())) {
            query = query.and(FileRepository.byCustomFilter(filter.getCustomFilter()));
        }

        return query;
    }

    private void validateIfAlreadyExists(HarvestFile harvestFile, @NonNull UploadForm form) throws FileAlreadyExistsException {
        if(!harvestFile.getName().equalsIgnoreCase(form.getName())){
            validateIfAlreadyExists(harvestFile);
        }
    }

    private void validateIfAlreadyExists(@NonNull HarvestFile harvestFile) throws FileAlreadyExistsException {
        if(fileRepository.existsByNameAndAuthor_Client(harvestFile.getName(), harvestFile.getAuthor().getClient())){
            throw new FileAlreadyExistsException(harvestFile.getName() + " already exists.");
        }
    }

    @Override
    public void update(@NonNull String id, @NonNull UploadForm form, @NonNull String token) throws Exception {

        HarvestFile harvestFile = getFileByIdAndAuthorizedClient(id, token, true);

        Boolean needToUploadFile = Objects.nonNull(form.getFile());
        Boolean needToUploadPreview = Objects.nonNull(form.getPreviewImage());


        if(needToUploadFile){
            validateIfAlreadyExists(harvestFile, form);
        }

        HarvestFile updatedHarvestFile = convertFormToFile(form, token);

        BeanUtils.copyProperties(updatedHarvestFile, harvestFile, "id", "author", "createdAt", "createdBy",
                !needToUploadFile ? "mimeType" : "",
                !needToUploadFile ? "contentId" : "",
                !needToUploadFile ? "contentLength" : "",
                !needToUploadFile ? "contentPath" : "",
                !needToUploadFile ? "fileName" : "",
                !needToUploadPreview ? "previewImagePath" : "");


        if(needToUploadFile){
            uploadFile(harvestFile, form);
        }

        if(needToUploadPreview){
            uploadPreviewImage(harvestFile, form);
        }

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

    public HarvestFile getFileByIdAndAuthorizedClient(@NonNull String id, @NonNull String token, Boolean validateAuthor) throws Exception {

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
        } catch (IOException e) {
            log.error("An error occurs while uploading the file", e);
            throw new GenericUploadException(e.getMessage(), e.getCause());
        }
    }

    private void uploadPreviewImage(@NonNull HarvestFile harvestFile, @NonNull UploadForm form){
        harvestFile.setPreviewImagePath(
                assetStorageService.uploadAsset(form.getPreviewImage(),
                        harvestFile.getAuthor().getClient().getCnpj(),
                        harvestFile.getName() + "_preview",
                        FileTypeEnum.THUMBNAIL, harvestFile.getAuthor()));
    }

    private String createFilePath(String fileName, Client client) {
        return MessageFormat.format("{0}/{1}/{2}", FILE_FOLDER, client.getCnpj(), fileName);
    }

    private HarvestFile convertFormToFile(@NonNull UploadForm form, @NonNull String token) {
        User user = userService.findUserByToken(token);

        String fileName = Objects.nonNull(form.getFile()) ?
                MessageFormat.format("{0}-{1}.{2}", form.getName(), GenericUtils.generateUUID(),
                        FilenameUtils.getExtension(form.getFile().getOriginalFilename())) : null;

        String mimeType = Objects.nonNull(form.getFile()) ? form.getFile().getContentType() : null;

        return HarvestFile.builder()
                .author(user)
                .name(form.getName())
                .description(form.getDescription())
                .authorizedClients(fetchService.generateAuthorizedClients(form.getAuthorizedClients(), user))
                .fileName(fileName)
                .contentPath(createFilePath(fileName, user.getClient()))
                .mimeType(mimeType).build();
    }

}