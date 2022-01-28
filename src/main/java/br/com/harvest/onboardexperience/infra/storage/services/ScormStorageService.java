package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
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
import org.springframework.util.ObjectUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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
    private ImageStorageService imageStorageService;

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
        scorm.setAuthorizedClients(generateAuthorizedClients(form.getAuthorizedClients(), author));
        return scorm;
    }


    @Override
    public void validate(@NonNull UploadForm form) {
        if(Objects.isNull(form.getFile())){
            throw new NullPointerException("The Scorm ZIP cannot be null.");
        }
    }

    @Override
    public Page<ScormDto> findAll(@NonNull String token, HarvestLibraryFilter filter, Pageable pageable) {
        return repository.findAll(buildQuery(filter, token), pageable).map(ScormMapper.INSTANCE::toDto);
    }

    private Specification<Scorm> buildQuery(@NonNull HarvestLibraryFilter filter, @NonNull String token){
        Specification<Scorm> query = Specification.where(ScormRepository.byAuthorizedClients(
                tenantService.fetchClientByTenantFromToken(token)
        ));

        if(Objects.nonNull(filter.getCriteriaFilter())){
            query = query.and(ScormRepository.byCustomFilter(filter.getCriteriaFilter()));
        }

        if(Objects.nonNull(filter.getCourseLearningStandard())){
            query = query.and(ScormRepository.byScormLearningStandard(filter.getCourseLearningStandard()));
        }

        return query;
    }

    private void uploadImage(@NonNull Scorm scorm, @NonNull UploadForm form) {
        scorm.setPreviewImagePath(
                imageStorageService.uploadImage(form.getPreviewImage(),
                        scorm.getAuthor().getClient().getCnpj(),
                        MessageFormat.format("{0}_{1}_preview",  scorm.getTitle(), GenericUtils.generateUUID()),
                        FileTypeEnum.IMAGE, scorm.getAuthor()));
    }

    @Override
    public void update(@NonNull Long id, @NonNull UploadForm form, @NonNull String token) throws Exception {

    }

    @Override
    public void delete(@NonNull Long id, @NonNull String token) throws Exception {
        Scorm scorm = find(id, token, true);
        scormService.deleteScormCourse(scorm.getId());
        scorm.getRegistrations().stream().map(ScormRegistration::getId).forEach(registrationService::disableRegister);
        repository.delete(scorm);
    }

    @Override
    public Optional<ScormDto> find(@NonNull Long id, @NonNull String token) throws Exception {
        Scorm scorm = find(id, token, false);

        ScormDto dto = ScormMapper.INSTANCE.toDto(scorm);
        dto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(scorm.getAuthorizedClients(), Client.class));
        dto.setStorage(Storage.SCORM);

        return Optional.of(dto);
    }

    private Scorm find(@NonNull Long id, @NonNull String token, Boolean validateAuthor) throws Exception {
        User user = userService.findUserByToken(token);

        Scorm scorm = repository.findOne(createFindOneQuery(id, user))
                .orElseThrow(
                        () -> new FileNotFoundException("The requested file doesn't exist or you don't have access to get it."));

        if(validateAuthor){
            StorageService.validateAuthor(scorm, Scorm.class, user, "You're not the author of the SCORM file.");
        }

        return scorm;
    }

    private Specification<Scorm> createFindOneQuery(@NonNull Long id, @NonNull User user){
        Specification<Scorm> queryByIdAndAuthorizedClient = ScormRepository.byScormIdEqual(id.toString())
                .and(ScormRepository.byAuthorizedClients(user.getClient()));

        Specification<Scorm> queryByIdAndAuthor = ScormRepository.byScormIdEqual(id.toString())
                .and(ScormRepository.byAuthor(user));
        return queryByIdAndAuthorizedClient.or(queryByIdAndAuthor);
    }

    @Override
    public void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull List<Long> authorizedClients) throws Exception {
        Scorm scorm = find(id, token, true);

        scorm.setAuthorizedClients(fetchService.fetchClients(authorizedClients));

        repository.save(scorm);
    }

    private List<Client> generateAuthorizedClients(List<Long> clientsId, @NonNull User user){
        if(ObjectUtils.isEmpty(clientsId)){
            clientsId = new ArrayList<>();
        }

        clientsId.add(user.getClient().getId());

        return fetchService.fetchClients(clientsId);
    }
}
