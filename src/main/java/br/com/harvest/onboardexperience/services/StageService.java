package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.*;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.entities.keys.*;
import br.com.harvest.onboardexperience.domain.exceptions.AlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import br.com.harvest.onboardexperience.infra.scorm.services.ScormService;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.services.HarvestFileStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.LinkStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.ScormStorageService;
import br.com.harvest.onboardexperience.mappers.StageMapper;
import br.com.harvest.onboardexperience.mappers.StageUserMapper;
import br.com.harvest.onboardexperience.repositories.*;
import br.com.harvest.onboardexperience.domain.dtos.forms.StageForm;
import br.com.harvest.onboardexperience.usecases.UserCoinUseCase;
import com.rusticisoftware.cloud.v2.client.model.RegistrationSchema;
import com.rusticisoftware.cloud.v2.client.model.RegistrationSuccess;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
    private StageUserRepository stageUserRepository;

    @Autowired
    private ScormService scormService;

    @Autowired
    private UserCoinUseCase userCoinUseCase;

    public StageDTO create(@NonNull Long trailId, StageForm form, @NonNull String token){
        validate(trailId, form, token);
        Stage stage = repository.save(formToStage(trailId, form, token));
        associateMediasToStage(form.getMedias(), stage, token);
        return StageMapper.INSTANCE.toDto(stage);
    }

    public StageDTO update(@NonNull Long trailId, @NonNull Long stageId, @NonNull StageForm form, @NonNull String token){
        Stage stage = findAsAdmin(trailId, stageId, token);

        validate(stage, trailId, form, token);

        Stage updatedStage = formToStage(trailId, form, token);

        BeanUtils.copyProperties(updatedStage, stage, "id", "trail", "position", "scorms", "files", "links");

        associateMediasToStage(form.getMedias(), stage, token);

        return StageMapper.INSTANCE.toDto(repository.save(stage));
    }

    public void startStage(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){

        trailService.validateIfUserStartedTheTrail(trailId, token);

        Stage stage = findAsColaborator(trailId, stageId, token);

        User user = userService.findUserByToken(token);

        if(!stageUserRepository.existsById(createStageUserId(user, stage))){
            stageUserRepository.save(createStageUser(user, stage));
            stage.getLinks().forEach(link -> startLinkMedia(trailId, stageId, link.getLink().getId().toString(), token));
            stage.getFiles().forEach(file -> startHarvestFileMedia(trailId, stageId, file.getHarvestFile().getId().toString(), token));
            stage.getScorms().forEach(scorm -> startScormMedia(trailId, stageId, scorm.getScorm().getId(), token));
        }
    }

    public void deleteMedias(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        Stage stage = findAsAdmin(trailId, stageId, token);

        stage.getFiles().forEach(file -> disassociateHarvestFilesToStage(file.getHarvestFile().getId(), stage, token));
        stage.getLinks().forEach(link -> disassociateLinkMediaToStage(link.getLink().getId(), stage, token));
        stage.getScorms().forEach(scorm -> disassociateScormMediaToStage(scorm.getScorm().getId(), stage, token));
    }

    public StageUserSimpleDTO finishStage(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        Stage stage = findAsColaborator(trailId, stageId, token);

        User user = userService.findUserByToken(token);

        StageUser stageUser = stageUserRepository.findById(createStageUserId(user, stage)).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any register of the stage's ID {1} " +
                        "start from user ID {1}", stage.getId(), user.getId()))
        );

        if(!stageUser.getIsCompleted()){
            stageUser.setScore(calculateUserScore(stageUser));

            if(stageUser.getScore().compareTo(stageUser.getStage().getMinimumScore()) >= 0){
                stageUser.setCompletedAt(LocalDateTime.now());
                stageUser.setIsCompleted(true);
                userCoinUseCase.addCoinToUser(userCoinUseCase.createAddCoinsForm(stageUser.getUser(),
                        stageUser.getStage().getTrail().getCoin(), stageUser.getStage().getAmountCoins()), token);
            }

        }
        return StageUserMapper.INSTANCE.toSimpleDTO(stageUserRepository.save(stageUser));
    }

    public List<StageUser> getAllStagesFromUserInTrail(@NonNull User user, @NonNull Trail trail){
        return stageUserRepository.findAll(StageUserRepository.byUserAndTrail(user, trail));
    }

    private BigDecimal calculateUserScore(@NonNull StageUser stageUser){
        BigInteger totalMedia = BigInteger.ZERO;
        BigInteger totalMediaPassed = BigInteger.ZERO;

        totalMedia = totalMedia.add(BigInteger.valueOf(stageUser.getScorms().size()));
        totalMedia = totalMedia.add(BigInteger.valueOf(stageUser.getHarvestFiles().size()));
        totalMedia = totalMedia.add(BigInteger.valueOf(stageUser.getLinks().size()));

        totalMediaPassed = totalMediaPassed.add(BigInteger.valueOf(stageUser.getScorms().stream().filter(ScormMediaUser::getIsCompleted).count()));
        totalMediaPassed = totalMediaPassed.add(BigInteger.valueOf(stageUser.getHarvestFiles().stream().filter(HarvestFileMediaUser::getIsCompleted).count()));
        totalMediaPassed = totalMediaPassed.add(BigInteger.valueOf(stageUser.getLinks().stream().filter(LinkMediaUser::getIsCompleted).count()));

        return new BigDecimal(totalMediaPassed).divide(new BigDecimal(totalMedia.compareTo(BigInteger.ZERO) == 0
                        ? BigInteger.ONE : totalMedia), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
    }

    private StageUserId createStageUserId(@NonNull User user, @NonNull Stage stage){
        return StageUserId.builder()
                .stage(stage.getId())
                .user(user.getId())
                .build();
    }

    private StageUser createStageUser(@NonNull User user, @NonNull Stage stage){
        return StageUser.builder()
                .user(user)
                .stage(stage)
                .startedAt(LocalDateTime.now())
                .build();
    }

    public void delete(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        repository.delete(findAsAdmin(trailId, stageId, token));
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
                .previewImagePath(scorm.getScorm().getPreviewImagePath())
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
                .previewImagePath(file.getHarvestFile().getPreviewImagePath())
                .contentPath(file.getHarvestFile().getContentPath())
                .build();
    }

    private MediaExecution createLinkMediaExecution(LinkMediaStage link){
        return MediaExecution
                .builder()
                .id(link.getLink().getId().toString())
                .previewImagePath(link.getLink().getPreviewImagePath())
                .creationDate(link.getCreatedAt())
                .storage(Storage.LINK)
                .build();
    }

    public List<StageDTO> findAllByTrailAsColaborator(@NonNull Long trailId, @NonNull String token){
        return trailService.findTrailByIdAndEndUserByTokenAsColaborator(trailId, token)
                .getStages().stream().map(this::stageToDto).collect(Collectors.toList());
    }

    public List<StageDTO> findAllByTrailAsAdmin(@NonNull Long trailId, @NonNull String token){
        return trailService.findTrailByIdAndTokenAsAdmin(trailId, token)
                .getStages().stream().map(this::stageToDto).collect(Collectors.toList());
    }

    public StageDTO findStageByPosition(@NonNull Long trailId, @NonNull PositionDTO position, @NonNull String token){
        return repository.findOne(StageRepository.byPositionAndTrail(positionService.getPosition(position),
                trailService.findTrailByIdAndToken(trailId, token)))
                .map(this::stageToDto)
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

    private void startScormMedia(@NonNull Long trailId, @NonNull Long stageId, @NonNull String scormId, @NonNull String token){
        Optional<ScormMediaStage> scormMediaStage = scormMediaStageRepository.findOne(ScormMediaStageRepository.byStageAndScorm(
            findAsColaborator(trailId, stageId, token), scormStorageService.find(scormId, token, false)
        ));

        scormMediaStage.ifPresent(mediaStage -> startScormMediaUserExecution(mediaStage, token));
    }

    private void startHarvestFileMedia(@NonNull Long trailId, @NonNull Long stageId, @NonNull String harvestFileId, @NonNull String token) {
        HarvestFile harvestFile = null;

        try {
            harvestFile = harvestFileStorageService.getFileByIdAndAuthorizedClient(harvestFileId, token, false);
        } catch (Exception e){
            log.info("An error was occurred", e);
        }

        Optional<HarvestFileMediaStage> harvestFileMediaStage = harvestFileMediaStageRepository.findOne(HarvestFileMediaStageRepository.byStageAndHarvestFile(
                findAsColaborator(trailId, stageId, token), harvestFile
        ));

        harvestFileMediaStage.ifPresent(mediaStage -> startHarvestFileMediaUserExecution(mediaStage, token));
    }

    private void startLinkMedia(@NonNull Long trailId, @NonNull Long stageId, @NonNull String linkMediaId, @NonNull String token) {
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

    private void startHarvestFileMediaUserExecution(@NonNull HarvestFileMediaStage harvestFileMediaStage, @NonNull String token){

        User user = userService.findUserByToken(token);

        if(!harvestFileMediaUserRepository.existsById(createHarvestFileMediaUserId(harvestFileMediaStage, user))){
            harvestFileMediaUserRepository.save(createHarvestMediaUser(harvestFileMediaStage, user));
        }
    }

    private void startLinkMediaUserExecution(@NonNull LinkMediaStage linkMediaStage, @NonNull String token){

        User user = userService.findUserByToken(token);

        if(!linkMediaUserRepository.existsById(createLinkMediaUserId(linkMediaStage, user))){
            linkMediaUserRepository.save(createLinkMediaUser(linkMediaStage, user));
        }
    }

    private void finishScormMediaExecution(@NonNull Long trailId, @NonNull Long stageId, @NonNull String scormId, @NonNull String token) throws Exception {
        User user = userService.findUserByToken(token);

        ScormMediaStage scormMediaStage = scormMediaStageRepository.findOne(ScormMediaStageRepository.byStageAndScorm(
                findAsColaborator(trailId, stageId, token), scormStorageService.find(scormId, token, false)
        )).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any register of SCORM as a media in Stage ID {1}", stageId))
        );

        ScormMediaUser scormMediaUser = scormMediaUserRepository.findById(createScormMediaUserId(scormMediaStage, user)).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any start register of SCORM execution from user ID {1}", user.getId()))
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

    private void finishHarvestFileMediaExecution(@NonNull Long trailId, @NonNull Long stageId, @NonNull String harvestFileId, @NonNull String token) throws Exception {
        User user = userService.findUserByToken(token);

        HarvestFileMediaStage harvestFileMediaStage = harvestFileMediaStageRepository.findOne(HarvestFileMediaStageRepository.byStageAndHarvestFile(
                findAsColaborator(trailId, stageId, token), harvestFileStorageService.getFileByIdAndAuthorizedClient(harvestFileId, token, false)
        )).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any register of Harvest File as a media in Stage ID {1}", stageId))
        );

        HarvestFileMediaUser harvestFileMediaUser = harvestFileMediaUserRepository.findById(createHarvestFileMediaUserId(harvestFileMediaStage, user)).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any start register of Harvest File execution from user ID {1}", user.getId()))
        );

        if(!harvestFileMediaUser.getIsCompleted()){
            harvestFileMediaUser.setCompletedAt(LocalDateTime.now());
            harvestFileMediaUser.setIsCompleted(true);
            harvestFileMediaUserRepository.save(harvestFileMediaUser);
        }
    }

    private void finishLinkMediaExecution(@NonNull Long trailId, @NonNull Long stageId, @NonNull String linkId, @NonNull String token) {
        User user = userService.findUserByToken(token);

        LinkMediaStage linkMediaStage = linkMediaStageRepository.findOne(LinkMediaStageRepository.byStageAndLink(
                findAsColaborator(trailId, stageId, token), linkStorageService.getLinkByIdAndAuthorizedClient(linkId, token, false)
        )).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any register of Link as a media in Stage ID {1}", stageId))
        );

        LinkMediaUser linkMediaUser = linkMediaUserRepository.findById(createLinkMediaUserId(linkMediaStage, user)).orElseThrow(
                () -> new NotFoundException(MessageFormat.format("There isn't any start register of Link execution from user ID {1}", user.getId()))
        );

        if(!linkMediaUser.getIsCompleted()){
            linkMediaUser.setCompletedAt(LocalDateTime.now());
            linkMediaUser.setIsCompleted(true);
            linkMediaUserRepository.save(linkMediaUser);
        }
    }

    private ScormMediaStageId createScormMediaId(@NonNull String scormId, @NonNull Long stageId){
        return ScormMediaStageId.builder()
                .scorm(scormId)
                .stage(stageId)
                .build();
    }

    private ScormMediaUserId createScormMediaUserId(@NonNull ScormMediaStage scormMediaStage, @NonNull User user){
        return ScormMediaUserId.builder()
                .user(user.getId())
                .scormMedia(createScormMediaId(scormMediaStage.getScorm().getId(), scormMediaStage.getStage().getId()))
                .build();
    }

    private LinkMediaStageId createLinkMediaStageId(@NonNull Long linkId, @NonNull Long stageId){
        return LinkMediaStageId.builder()
                .link(linkId)
                .stage(stageId)
                .build();
    }
    private LinkMediaUserId createLinkMediaUserId(@NonNull LinkMediaStage linkMediaStage, @NonNull User user){
        return LinkMediaUserId.builder()
                .user(user.getId())
                .linkMedia(createLinkMediaStageId(linkMediaStage.getLink().getId(), linkMediaStage.getStage().getId()))
                .build();
    }

    private HarvestFileMediaStageId createHarvestMediaStageId(@NonNull Long fileId, @NonNull Long stageId){
        return HarvestFileMediaStageId.builder()
                .harvestFile(fileId)
                .stage(stageId)
                .build();
    }

    private HarvestFileMediaUserId createHarvestFileMediaUserId(@NonNull HarvestFileMediaStage harvestFileMediaStage, @NonNull User user){
        return HarvestFileMediaUserId.builder()
                .user(user.getId())
                .harvestFileMedia(createHarvestMediaStageId(harvestFileMediaStage.getHarvestFile().getId(), harvestFileMediaStage.getStage().getId()))
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

    private void validate(@NonNull Long trailId, StageForm stageForm, String token){
        validateIfAlreadyHasAStageOnPosition(trailId, stageForm, token);
        validateIfAlreadyHasAStageWithThisName(trailId, stageForm, token);
        validateIfPositionIsMappedInTrail(trailId, stageForm, token);
    }

    private void validate(@NonNull Stage stage, @NonNull Long trailId, StageForm stageForm, String token){

        if(!stage.getName().equalsIgnoreCase(stageForm.getName())){
            validateIfAlreadyHasAStageWithThisName(trailId, stageForm, token);
        }

        if(!stage.getPosition().getXAxis()
                .equals(stageForm.getPosition().getXAxis())
         && !stage.getPosition().getYAxis().equals(stageForm.getPosition().getYAxis())){
            validateIfAlreadyHasAStageOnPosition(trailId, stageForm, token);
            validateIfPositionIsMappedInTrail(trailId, stageForm, token);
        }
    }

    private void validateIfPositionIsMappedInTrail(@NonNull Long trailId, StageForm stageForm, String token){
        Trail trail = trailService.findTrailByIdAndTokenAsAdmin(trailId, token);

        if(trail.getCharacterMapPositionPath().stream().noneMatch(position ->
                position.getXAxis().equals(stageForm.getPosition().getXAxis()) && position.getYAxis().equals(stageForm.getPosition().getYAxis()))){
            throw new BusinessException("The position needs to be mapped on character path in Trail");
        }
    }

    private void validateIfAlreadyHasAStageOnPosition(@NonNull Long trailId, StageForm stageForm, String token){
        if(Objects.nonNull(findStageByPosition(trailId, stageForm.getPosition(), token))){
            throw new BusinessException("There is a stage mapped on this position already.");
        }
    }

    private void validateIfAlreadyHasAStageWithThisName(@NonNull Long trailId, StageForm stageForm, String token){
        if(findByNameAndTrail(stageForm.getName(), trailId, token).isPresent()){
            throw new AlreadyExistsException("Stage", "name", stageForm.getName());
        }
    }

    private Optional<Stage> findByNameAndTrail(@NonNull String name, @NonNull Long trailId, @NonNull String token){
        return repository.findOne(StageRepository.byNameAndTrail(name, trailService.findTrailByIdAndTokenAsAdmin(trailId, token)));
    }

    private void associateScormMediaToStage(String scormsId, @NonNull Stage stage, @NonNull String token){
        if(StringUtils.hasText(scormsId)){
            if(!scormMediaStageRepository.existsById(createScormMediaId(scormsId, stage.getId()))){
                scormMediaStageRepository.save(createScormMedia(scormsId, stage, token));
            }
        }
    }

    private void disassociateScormMediaToStage(String scormsId, @NonNull Stage stage, @NonNull String token){
        if(StringUtils.hasText(scormsId)){
            if(scormMediaStageRepository.existsById(createScormMediaId(scormsId, stage.getId()))){
                scormMediaStageRepository.delete(createScormMedia(scormsId, stage, token));
            }
        }
    }

    private void disassociateHarvestFilesToStage(Long filesId, @NonNull Stage stage, @NonNull String token)  {
        if(Objects.nonNull(filesId)){
            if(harvestFileMediaStageRepository.existsById(createHarvestMediaStageId(filesId, stage.getId()))){
                try {
                    harvestFileMediaStageRepository.delete(createHarvestFileMedia(filesId, stage, token));
                } catch (Exception e) {
                    log.info("An error was occurred: ", e);
                }
            }
        }
    }

    private void disassociateLinkMediaToStage(Long linksId, @NonNull Stage stage, @NonNull String token){
        if(Objects.nonNull(linksId)){
            if(linkMediaStageRepository.existsById(createLinkMediaStageId(linksId, stage.getId()))){
                linkMediaStageRepository.delete(createLinkMedia(linksId, stage, token));
            }
        }
    }

    private void associateHarvestFilesToStage(Long filesId, @NonNull Stage stage, @NonNull String token) throws Exception {
        if(Objects.nonNull(filesId)){
            if(!harvestFileMediaStageRepository.existsById(createHarvestMediaStageId(filesId, stage.getId()))){
                harvestFileMediaStageRepository.save(createHarvestFileMedia(filesId, stage, token));
            }
        }
    }

    private void associateLinkMediaToStage(Long linksId, @NonNull Stage stage, @NonNull String token){
        if(Objects.nonNull(linksId)){
            if(!linkMediaStageRepository.existsById(createLinkMediaStageId(linksId, stage.getId()))){
                linkMediaStageRepository.save(createLinkMedia(linksId, stage, token));
            }
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
