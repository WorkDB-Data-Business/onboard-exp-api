package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.domain.exceptions.ScormCourseNotFoundException;
import br.com.harvest.onboardexperience.infra.scorm.dtos.ScormDto;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import br.com.harvest.onboardexperience.infra.scorm.mappers.ScormMapper;
import br.com.harvest.onboardexperience.infra.scorm.repository.ScormRepository;
import br.com.harvest.onboardexperience.infra.scorm.services.RegistrationService;
import br.com.harvest.onboardexperience.infra.scorm.services.ScormService;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.filters.HarvestLibraryFilter;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import com.rusticisoftware.cloud.v2.client.ApiException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
public class ScormStorageService implements StorageService {

    @Autowired
    private ScormService scormService;

    @Autowired
    private UserService userService;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ScormRepository repository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AssetStorageService assetStorageService;

    private final Function<ScormDto, ScormDto> SET_STORAGE = scormDto -> {
        scormDto.setStorage(Storage.SCORM);
        return scormDto;
    };

    @Override
    public void save(@NonNull UploadForm form, @NonNull String token) {
        try {
            Scorm scorm = convertFormToScorm(scormService.importScormCourse(form.getFile()), form, token);
            uploadImage(scorm, form);
            repository.save(scorm);
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
    }

    private Scorm convertFormToScorm(Scorm scorm, UploadForm form, @NonNull String token){
        User author = userService.findUserByToken(token);
        scorm.setAuthor(author);
        scorm.setAuthorizedClients(fetchService.generateAuthorizedClients(form.getAuthorizedClients(), author));
        return scorm;
    }


    @Override
    public void validate(@NonNull UploadForm form) {
        if(Objects.isNull(form.getFile())){
            throw new GenericUploadException("The Scorm ZIP cannot be null.");
        }

        if(Objects.isNull(form.getPreviewImage())){
            throw new GenericUploadException("The image preview cannot be null.");
        }
    }

    @Override
    public Page<ScormDto> findAll(@NonNull String token, HarvestLibraryFilter filter, Pageable pageable) {
        return repository.findAll(buildQuery(filter, token), pageable).map(ScormMapper.INSTANCE::toDto).map(SET_STORAGE);
    }

    public List<Scorm> findAllByClient(@NonNull Client client) {
        return repository.findAll(ScormRepository.byClient(client));
    }

    private Specification<Scorm> buildQuery(@NonNull HarvestLibraryFilter filter, @NonNull String token){
        Specification<Scorm> query = Specification.where(ScormRepository.byAuthorizedClients(
                tenantService.fetchClientByTenantFromToken(token)
        ));

        if(StringUtils.hasText(filter.getCustomFilter())){
            query = query.and(ScormRepository.byCustomFilter(filter.getCustomFilter()));
        }

        if(Objects.nonNull(filter.getCourseLearningStandard())){
            query = query.and(ScormRepository.byScormLearningStandard(filter.getCourseLearningStandard()));
        }

        return query;
    }

    private void uploadImage(@NonNull Scorm scorm, @NonNull UploadForm form) {
        scorm.setPreviewImagePath(
                assetStorageService.uploadAsset(form.getPreviewImage(),
                        scorm.getAuthor().getClient().getCnpj(),
                        MessageFormat.format("{0}_{1}_preview",  scorm.getTitle(), GenericUtils.generateUUID()),
                        FileTypeEnum.ASSET, scorm.getAuthor()));
    }

    @Override
    public void update(@NonNull String id, @NonNull UploadForm form, @NonNull String token) throws Exception {
        Scorm scorm = find(id, token, true);

        if(Objects.nonNull(form.getPreviewImage())){
            uploadImage(scorm, form);
        }

        repository.save(scorm);
    }

    @Override
    public void delete(@NonNull String id, @NonNull String token) throws Exception {
        Scorm scorm = find(id, token, true);
        scormService.deleteScormCourse(scorm.getId());
        scorm.getRegistrations().stream().map(ScormRegistration::getId).forEach(registrationService::disableRegister);
        repository.delete(scorm);
    }

    @Override
    public Optional<ScormDto> find(@NonNull String id, @NonNull String token) throws Exception {
        Scorm scorm = find(id, token, false);

        ScormDto dto = ScormMapper.INSTANCE.toDto(scorm);
        dto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(scorm.getAuthorizedClients(), Client.class));
        dto.setStorage(Storage.SCORM);

        return Optional.of(dto);
    }

    public Scorm find(@NonNull String id, @NonNull String token, Boolean validateAuthor) {
        User user = userService.findUserByToken(token);

        Scorm scorm = repository.findOne(createFindOneQuery(id, user))
                .orElseThrow(
                        () -> new ScormCourseNotFoundException("The requested SCORM file doesn't exist or you don't have access to get it."));

        if(validateAuthor){
            StorageService.validateAuthor(scorm, Scorm.class, user, "You're not the author of the SCORM file.");
        }


        return scorm;
    }

    private Specification<Scorm> createFindOneQuery(@NonNull String id, @NonNull User user){
        Specification<Scorm> queryByIdAndAuthorizedClient = ScormRepository.byScormIdEqual(id)
                .and(ScormRepository.byAuthorizedClients(user.getClient()));

        Specification<Scorm> queryByIdAndAuthor = ScormRepository.byScormIdEqual(id)
                .and(ScormRepository.byAuthor(user));

        return queryByIdAndAuthorizedClient.or(queryByIdAndAuthor);
    }

    @Override
    public void updateAuthorizedClients(@NonNull String id, @NonNull String token, @NonNull List<Long> authorizedClients) throws Exception {
        Scorm scorm = find(id, token, true);

        scorm.setAuthorizedClients(fetchService.fetchClients(authorizedClients));

        repository.save(scorm);
    }
}
