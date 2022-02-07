package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.*;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.entities.keys.*;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import br.com.harvest.onboardexperience.infra.scorm.services.ScormService;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.services.HarvestFileStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.LinkStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.ScormStorageService;
import br.com.harvest.onboardexperience.mappers.StageMapper;
import br.com.harvest.onboardexperience.repositories.*;
import br.com.harvest.onboardexperience.domain.dtos.forms.StageForm;
import com.rusticisoftware.cloud.v2.client.model.RegistrationSchema;
import com.rusticisoftware.cloud.v2.client.model.RegistrationSuccess;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StageService {

    @Autowired
    private StageRepository repository;

    @Autowired
    private ScormMediaStageRepository scormMediaStageRepository;

    @Autowired
    private ScormMediaUserRepository scormMediaUserRepository;

    @Autowired
    private TrailService trailService;

    @Autowired
    private ScormStorageService scormStorageService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private HarvestFileStorageService harvestFileStorageService;

    @Autowired
    private HarvestFileMediaStageRepository harvestFileMediaStageRepository;

    @Autowired
    private HarvestFileMediaUserRepository harvestFileMediaUserRepository;

    @Autowired
    private LinkMediaStageRepository linkMediaStageRepository;

    @Autowired
    private LinkMediaUserRepository linkMediaUserRepository;

    @Autowired
    private LinkStorageService linkStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScormService scormService;

    public StageDTO create(@NonNull Long trailId, StageForm form, @NonNull String token){
        Stage stage = repository.save(formToStage(trailId, form, token));
        associateMediasToStage(form.getMedias(), stage, token);
        return StageMapper.INSTANCE.toDto(stage);
    }

    public StageDTO update(@NonNull Long trailId, @NonNull Long stageId, StageForm form, @NonNull String token){
        Stage stage = findAsAdmin(trailId, stageId, token);
        Stage updatedStage = formToStage(trailId, form, token);

        BeanUtils.copyProperties(updatedStage, stage, "id", "trail", "position", "scorms", "files", "links");

        associateMediasToStage(form.getMedias(), stage, token);

        return StageMapper.INSTANCE.toDto(stage);
    }

    private void associateMediasToStage(@NonNull List<MediaExecution> medias, @NonNull Stage stage, @NonNull String token){
        if(ObjectUtils.isNotEmpty(medias)){
            medias.forEach(media -> associateMediaToStage(media, stage, token));
        }
    }

    private void associateMediaToStage(@NonNull MediaExecution mediaExecution, @NonNull Stage stage, @NonNull String token) {
        switch (mediaExecution.getStorage()){
            case SCORM: {
                associateScormMediaToStage(mediaExecution.getId(), stage, token);
                break;
            }
            case LINK: {
                associateLinkMediaToStage(Long.parseLong(mediaExecution.getId()), stage, token);
                break;
            } case HARVEST_FILE: {
                try {
                    associateHarvestFilesToStage(Long.parseLong(mediaExecution.getId()), stage, token);
                } catch (Exception e){
                    log.info("An error occurred:", e);
                }
                break;
            }
        }
    }

    public StageDTO findAsAdminAsDTO(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        return stageToDto(findAsAdmin(trailId, stageId, token));
    }

    public Stage findAsAdmin(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        return repository.findOne(StageRepository.byIdAndTrail(stageId, trailService.findTrailByIdAndTokenAsAdmin(trailId, token)))
                .orElseThrow(() -> new NotFoundException("Stage", "ID", stageId.toString()));
    }
    public StageDTO findAsColaboratorAsDTO(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        return stageToDto(findAsColaborator(trailId, stageId, token));
    }

    private Stage findAsColaborator(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        return repository.findOne(StageRepository.byIdAndTrail(stageId, trailService.findTrailByIdAndEndUserByTokenAsColaborator(trailId, token)))
                .orElseThrow(() -> new NotFoundException("Stage", "ID", stageId.toString()));
    }

    private StageDTO stageToDto(@NonNull Stage stage){
        StageDTO dto = StageMapper.INSTANCE.toDto(stage);
        List<MediaExecution> executions = new ArrayList<>();
        executions.addAll(stage.getScorms().stream().map(this::createScormMediaExecution).collect(Collectors.toList()));
        executions.addAll(stage.getFiles().stream().map(this::createHarvestFileMediaExecution).collect(Collectors.toList()));
        executions.addAll(stage.getLinks().stream().map(this::createLinkMediaExecution).collect(Collectors.toList()));
        dto.setMediaExecutions(executions.stream().sorted(Comparator.comparing(MediaExecution::getCreationDate)).collect(Collectors.toList()));
        return dto;
    }

    private MediaExecution createScormMediaExecution(ScormMediaStage scorm){
        return MediaExecution
                .builder()
                .id(scorm.getScorm().getId())
                .creationDate(scorm.getCreatedAt())
                .storage(Storage.SCORM)
                .build();
    }

    private MediaExecution createHarvestFileMediaExecution(HarvestFileMediaStage file){
        return MediaExecution
                .builder()
                .id(file.getHarvestFile().getId().toString())
                .creationDate(file.getCreatedAt())
                .storage(Storage.HARVEST_FILE)
                .build();
    }

    private MediaExecution createLinkMediaExecution(LinkMediaStage link){
        return MediaExecution
                .builder()
                .id(link.getLink().getId().toString())
                .creationDate(link.getCreatedAt())
                .storage(Storage.LINK)
                .build();
    }

    public List<StageDTO> findAllByTrailAsColaborator(@NonNull Long trailId, @NonNull String token){
        return trailService.findTrailByIdAndEndUserByTokenAsColaborator(trailId, token)
                .getStages().stream().map(StageMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public List<StageDTO> findAllByTrailAsAdmin(@NonNull Long trailId, @NonNull String token){
        return trailService.findTrailByIdAndTokenAsAdmin(trailId, token)
                .getStages().stream().map(StageMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public StageDTO findStageByPosition(@NonNull Long trailId, @NonNull PositionDTO position, @NonNull String token){
        return repository.findOne(StageRepository.byPositionAndTrail(positionService.getPosition(position),
                trailService.findTrailByIdAndEndUserByTokenAsColaborator(trailId, token)))
                .map(StageMapper.INSTANCE::toDto)
                .orElse(null);
    }

    public void startMedia(@NonNull Long trailId, @NonNull Long stageId,
                           @NonNull String mediaId, @NonNull Storage type,
                           @NonNull String token) throws Exception {
        switch (type){
            case HARVEST_FILE: {
                startHarvestFileMedia(trailId, stageId, mediaId, token);
                break;
            }
            case LINK: {
                startLinkMedia(trailId, stageId, mediaId, token);
                break;
            }
            case SCORM: {
                startScormMedia(trailId, stageId, mediaId, token);
                break;
            }
        }
    }

    public void finishMedia(@NonNull Long trailId, @NonNull Long stageId,
                           @NonNull String mediaId, @NonNull Storage type,
                           @NonNull String token) throws Exception {
        switch (type){
            case HARVEST_FILE: {
                finishHarvestFileMediaExecution(trailId, stageId, mediaId, token);
                break;
            }
            case LINK: {
                finishLinkMediaExecution(trailId, stageId, mediaId, token);
                break;
            }
            case SCORM: {
                finishScormMediaExecution(trailId, stageId, mediaId, token);
                break;
            }
        }
    }

    public void startScormMedia(@NonNull Long trailId, @NonNull Long stageId, @NonNull String scormId, @NonNull String token){
        Optional<ScormMediaStage> scormMediaStage = scormMediaStageRepository.findOne(ScormMediaStageRepository.byStageAndScorm(
            findAsColaborator(trailId, stageId, token), scormStorageService.find(scormId, token, false)
        ));

        scormMediaStage.ifPresent(mediaStage -> startScormMediaUserExecution(mediaStage, token));
    }

    public void startHarvestFileMedia(@NonNull Long trailId, @NonNull Long stageId, @NonNull String harvestFileId, @NonNull String token) throws Exception {
        Optional<HarvestFileMediaStage> harvestFileMediaStage = harvestFileMediaStageRepository.findOne(HarvestFileMediaStageRepository.byStageAndHarvestFile(
                findAsColaborator(trailId, stageId, token), harvestFileStorageService.getFileByIdAndAuthorizedClient(harvestFileId, token, false)
        ));

        harvestFileMediaStage.ifPresent(mediaStage -> startHarvestFileMediaUserExecution(mediaStage, token));
    }

    public void startLinkMedia(@NonNull Long trailId, @NonNull Long stageId, @NonNull String linkMediaId, @NonNull String token) {
        Optional<LinkMediaStage> linkMediaStage = linkMediaStageRepository.findOne(LinkMediaStageRepository.byStageAndLink(
                findAsColaborator(trailId, stageId, token), linkStorageService.getLinkByIdAndAuthorizedClient(linkMediaId, token, false)
        ));

        linkMediaStage.ifPresent(mediaStage -> startLinkMediaUserExecution(mediaStage, token));
    }

    public void startScormMediaUserExecution(@NonNull ScormMediaStage scormMediaStage, @NonNull String token){

        User user = userService.findUserByToken(token);

        if(!scormMediaUserRepository.existsById(createScormMediaUserId(scormMediaStage, user))){
            scormMediaUserRepository.save(createScormMediaUser(scormMediaStage, user));
        }
    }

    public void startHarvestFileMediaUserExecution(@NonNull HarvestFileMediaStage harvestFileMediaStage, @NonNull String token){

        User user = userService.findUserByToken(token);

        if(!harvestFileMediaUserRepository.existsById(createHarvestFileMediaUserId(harvestFileMediaStage, user))){
            harvestFileMediaUserRepository.save(createHarvestMediaUser(harvestFileMediaStage, user));
        }
    }

    public void startLinkMediaUserExecution(@NonNull LinkMediaStage linkMediaStage, @NonNull String token){

        User user = userService.findUserByToken(token);

        if(!linkMediaUserRepository.existsById(createLinkMediaUserId(linkMediaStage, user))){
            linkMediaUserRepository.save(createLinkMediaUser(linkMediaStage, user));
        }
    }

    public void finishScormMediaExecution(@NonNull Long trailId, @NonNull Long stageId, @NonNull String scormId, @NonNull String token) throws Exception {
        User user = userService.findUserByToken(token);

        ScormMediaStage scormMediaStage = scormMediaStageRepository.findOne(ScormMediaStageRepository.byStageAndScorm(
                findAsColaborator(trailId, stageId, token), scormStorageService.find(scormId, token, false)
        )).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any register of SCORM as a media in Stage ID {}", stageId))
        );

        ScormMediaUser scormMediaUser = scormMediaUserRepository.findById(createScormMediaUserId(scormMediaStage, user)).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any start register of SCORM execution from user ID {}", user.getId()))
        );

        ScormRegistration registration = scormService.findScormRegistrationByIdAndToken(scormMediaUser.getScormMedia().getScorm().getId(), token);

        RegistrationSuccess registrationStatus = scormService.getResultForRegistration(registration.getId()).map(RegistrationSchema::getRegistrationSuccess)
                .filter(registrationSuccess -> !registrationSuccess.equals(RegistrationSuccess.UNKNOWN))
                .orElseThrow(() -> new Exception("An error occurred while getting the registration status in SCORM CLOUD."));

        if(registrationStatus.equals(RegistrationSuccess.PASSED)){
            scormService.registerPassStatusOnScormRegistration(registration.getScorm().getId(), registration.getUser().getId());
            scormMediaUser.setCompletedAt(LocalDateTime.now());
            scormMediaUser.setIsCompleted(true);
            scormMediaUserRepository.save(scormMediaUser);
            scormService.deleteRegistration(scormMediaUser.getScormMedia().getScorm().getId(), user.getId());
        }

    }

    public void finishHarvestFileMediaExecution(@NonNull Long trailId, @NonNull Long stageId, @NonNull String harvestFileId, @NonNull String token) throws Exception {
        User user = userService.findUserByToken(token);

        HarvestFileMediaStage harvestFileMediaStage = harvestFileMediaStageRepository.findOne(HarvestFileMediaStageRepository.byStageAndHarvestFile(
                findAsColaborator(trailId, stageId, token), harvestFileStorageService.getFileByIdAndAuthorizedClient(harvestFileId, token, false)
        )).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any register of Harvest File as a media in Stage ID {}", stageId))
        );

        HarvestFileMediaUser harvestFileMediaUser = harvestFileMediaUserRepository.findById(createHarvestFileMediaUserId(harvestFileMediaStage, user)).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any start register of Harvest File execution from user ID {}", user.getId()))
        );

        if(!harvestFileMediaUser.getIsCompleted()){
            harvestFileMediaUser.setCompletedAt(LocalDateTime.now());
            harvestFileMediaUser.setIsCompleted(true);
            harvestFileMediaUserRepository.save(harvestFileMediaUser);
        }
    }

    public void finishLinkMediaExecution(@NonNull Long trailId, @NonNull Long stageId, @NonNull String linkId, @NonNull String token) {
        User user = userService.findUserByToken(token);

        LinkMediaStage linkMediaStage = linkMediaStageRepository.findOne(LinkMediaStageRepository.byStageAndLink(
                findAsColaborator(trailId, stageId, token), linkStorageService.getLinkByIdAndAuthorizedClient(linkId, token, false)
        )).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any register of Link as a media in Stage ID {}", stageId))
        );


        LinkMediaUser linkMediaUser = linkMediaUserRepository.findById(createLinkMediaUserId(linkMediaStage, user)).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any start register of Link execution from user ID {}", user.getId()))
        );

        if(!linkMediaUser.getIsCompleted()){
            linkMediaUser.setCompletedAt(LocalDateTime.now());
            linkMediaUser.setIsCompleted(true);
            linkMediaUserRepository.save(linkMediaUser);
        }
    }

    private ScormMediaUserId createScormMediaUserId(@NonNull ScormMediaStage scormMediaStage, @NonNull User user){
        ScormMediaStageId scormMediaStageId = ScormMediaStageId.builder()
                .scorm(scormMediaStage.getScorm().getId())
                .stage(scormMediaStage.getStage().getId())
                .build();

        return ScormMediaUserId.builder()
                .user(user.getId())
                .scormMedia(scormMediaStageId)
                .build();
    }

    private LinkMediaUserId createLinkMediaUserId(@NonNull LinkMediaStage linkMediaStage, @NonNull User user){
        LinkMediaStageId linkMediaStageId = LinkMediaStageId.builder()
                .link(linkMediaStage.getLink().getId())
                .stage(linkMediaStage.getStage().getId())
                .build();

        return LinkMediaUserId.builder()
                .user(user.getId())
                .linkMedia(linkMediaStageId)
                .build();
    }

    private HarvestFileMediaUserId createHarvestFileMediaUserId(@NonNull HarvestFileMediaStage harvestFileMediaStage, @NonNull User user){
        HarvestFileMediaStageId harvestFileMediaStageId = HarvestFileMediaStageId.builder()
                .harvestFile(harvestFileMediaStage.getHarvestFile().getId())
                .stage(harvestFileMediaStage.getStage().getId())
                .build();

        return HarvestFileMediaUserId.builder()
                .user(user.getId())
                .harvestFileMedia(harvestFileMediaStageId)
                .build();
    }


    private ScormMediaUser createScormMediaUser(@NonNull ScormMediaStage scormMediaStage, @NonNull User user){
        return ScormMediaUser
                .builder()
                .user(user)
                .scormMedia(scormMediaStage)
                .isCompleted(false)
                .build();
    }

    private LinkMediaUser createLinkMediaUser(@NonNull LinkMediaStage linkMediaStage, @NonNull User user){
        return LinkMediaUser
                .builder()
                .user(user)
                .linkMedia(linkMediaStage)
                .isCompleted(false)
                .build();
    }

    private HarvestFileMediaUser createHarvestMediaUser(@NonNull HarvestFileMediaStage harvestFileMediaStage, @NonNull User user){
        return HarvestFileMediaUser
                .builder()
                .user(user)
                .harvestFileMedia(harvestFileMediaStage)
                .isCompleted(false)
                .build();
    }

    private Stage formToStage(@NonNull Long trailId, StageForm form, @NonNull String token){
        return Stage.builder()
                .name(form.getName())
                .description(form.getDescription())
                .minimumScore(form.getMinimumScore())
                .trail(trailService.findTrailByIdAndToken(trailId, token))
                .amountCoins(form.getAmountCoins())
                .isPreRequisite(form.getIsPreRequisite())
                .position(positionService.getPosition(form.getPosition()))
                .build();
    }

    private void associateScormMediaToStage(String scormsId, @NonNull Stage stage, @NonNull String token){
        if(ObjectUtils.isNotEmpty(scormsId)){
            scormMediaStageRepository.save(createScormMedia(scormsId, stage, token));
        }
    }

    private void associateHarvestFilesToStage(Long filesId, @NonNull Stage stage, @NonNull String token) throws Exception {
        if(ObjectUtils.isNotEmpty(filesId)){
            harvestFileMediaStageRepository.save(createHarvestFileMedia(filesId, stage, token));
        }
    }

    private void associateLinkMediaToStage(Long linksId, @NonNull Stage stage, @NonNull String token){
        if(ObjectUtils.isNotEmpty(linksId)){
            linkMediaStageRepository.save(createLinkMedia(linksId, stage, token));
        }
    }

    private ScormMediaStage createScormMedia(String scormId, Stage stage, String token){
        return ScormMediaStage
                .builder()
                .scorm(scormStorageService.find(scormId, token, false))
                .stage(stage)
                .build();
    }

    private LinkMediaStage createLinkMedia(@NonNull Long linkId, @NonNull Stage stage, @NonNull String token){
        return LinkMediaStage
                .builder()
                .link(linkStorageService.getLinkByIdAndAuthorizedClient(linkId.toString(), token, false))
                .stage(stage)
                .build();
    }

    private HarvestFileMediaStage createHarvestFileMedia(@NonNull Long fileId, @NonNull Stage stage, @NonNull String token) throws Exception {
        return HarvestFileMediaStage
                .builder()
                .harvestFile(harvestFileStorageService.getFileByIdAndAuthorizedClient(fileId.toString(), token, false))
                .stage(stage)
                .build();
    }
}
