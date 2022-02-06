package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.ScormMediaStageDTO;
import br.com.harvest.onboardexperience.domain.dtos.StageDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.entities.keys.ScormMediaStageId;
import br.com.harvest.onboardexperience.domain.entities.keys.ScormMediaUserId;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.infra.storage.services.HarvestFileStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.LinkStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.ScormStorageService;
import br.com.harvest.onboardexperience.mappers.ScormMediaStageMapper;
import br.com.harvest.onboardexperience.mappers.StageMapper;
import br.com.harvest.onboardexperience.repositories.*;
import br.com.harvest.onboardexperience.domain.dtos.forms.StageForm;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    private ScormStorageService scormService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private HarvestFileStorageService harvestFileStorageService;

    @Autowired
    private HarvestFileMediaStageRepository harvestFileMediaStageRepository;

    @Autowired
    private LinkMediaStageRepository linkMediaStageRepository;

    @Autowired
    private LinkStorageService linkStorageService;

    @Autowired
    private UserService userService;

    public StageDTO create(StageForm form, @NonNull String token){
        Stage stage = repository.save(formToStage(form, token));
        associateScormMediasToStage(form.getScorms(), stage, token);
        associateHarvestFilesToStage(form.getFiles(), stage, token);
        associateLinkMediaToStage(form.getLinks(), stage, token);
        return StageMapper.INSTANCE.toDto(stage);
    }

    public StageDTO findAsAdmin(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        return repository.findOne(StageRepository.byIdAndTrail(stageId, trailService.findTrailByIdAndTokenAsAdmin(trailId, token)))
                .map(StageMapper.INSTANCE::toDto)
                .orElseThrow(() -> new NotFoundException("Stage", "ID", stageId.toString()));
    }

    public StageDTO findAsColaboratorAsDTO(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        return StageMapper.INSTANCE.toDto(findAsColaborator(trailId, stageId, token));
    }

    public Stage findAsColaborator(@NonNull Long trailId, @NonNull Long stageId, @NonNull String token){
        return repository.findOne(StageRepository.byIdAndTrail(stageId, trailService.findTrailByIdAndEndUserByTokenAsColaborator(trailId, token)))
                .orElseThrow(() -> new NotFoundException("Stage", "ID", stageId.toString()));
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

    public ScormMediaStageDTO startScormMedia(@NonNull Long trailId, @NonNull Long stageId, @NonNull String scormId, @NonNull String token){
        Optional<ScormMediaStage> scormMediaStage = scormMediaStageRepository.findOne(ScormMediaStageRepository.byStageAndScorm(
            findAsColaborator(trailId, stageId, token), scormService.find(scormId, token, false)
        ));

        scormMediaStage.ifPresent(mediaStage -> registerUserOnScormMedia(mediaStage, token));

        return scormMediaStage.map(ScormMediaStageMapper.INSTANCE::toDto).orElse(null);
    }

    private void registerUserOnScormMedia(@NonNull ScormMediaStage scormMediaStage, @NonNull String token){

        User user = userService.findUserByToken(token);

        ScormMediaStageId scormMediaStageId = ScormMediaStageId.builder()
                .scorm(scormMediaStage.getScorm().getId())
                .stage(scormMediaStage.getStage().getId())
                .build();

        ScormMediaUserId scormMediaUserId = ScormMediaUserId.builder()
                .user(user.getId())
                .scormMedia(scormMediaStageId)
                .build();

        if(!scormMediaUserRepository.existsById(scormMediaUserId)){
            scormMediaUserRepository.save(createScormMediaUser(scormMediaStage, user));
        }
    }

    private ScormMediaUser createScormMediaUser(@NonNull ScormMediaStage scormMediaStage, @NonNull User user){
        return ScormMediaUser
                .builder()
                .user(user)
                .scormMedia(scormMediaStage)
                .isCompleted(false)
                .build();
    }

    private Stage formToStage(StageForm form, @NonNull String token){
        return Stage.builder()
                .name(form.getName())
                .description(form.getDescription())
                .minimumScore(form.getMinimumScore())
                .amountCoins(form.getAmountCoins())
                .isPreRequisite(form.getIsPreRequisite())
                .position(positionService.getPosition(form.getPosition()))
                .trail(trailService.findTrailByIdAndTokenAsAdmin(form.getTrailId(), token))
                .build();
    }

    private void associateScormMediasToStage(List<String> scormsId, @NonNull Stage stage, @NonNull String token){
        if(ObjectUtils.isNotEmpty(scormsId)){
            scormMediaStageRepository.saveAll(createScormMediasInStage(scormsId, stage, token));
        }
    }

    private void associateHarvestFilesToStage(List<Long> filesId, @NonNull Stage stage, @NonNull String token){
        if(ObjectUtils.isNotEmpty(filesId)){
            harvestFileMediaStageRepository.saveAll(createHarvestFilesInStage(filesId, stage, token));
        }
    }

    private void associateLinkMediaToStage(List<Long> linksId, @NonNull Stage stage, @NonNull String token){
        if(ObjectUtils.isNotEmpty(linksId)){
            linkMediaStageRepository.saveAll(createLinkMediasInStage(linksId, stage, token));
        }
    }

    private List<ScormMediaStage> createScormMediasInStage(@NonNull List<String> scormsId, @NonNull Stage stage, @NonNull String token){
        return scormsId.stream().map(scormId -> createScormMedia(scormId, stage, token)).collect(Collectors.toList());
    }

    private List<LinkMediaStage> createLinkMediasInStage(@NonNull List<Long> linksId, @NonNull Stage stage, @NonNull String token){
        return linksId.stream().map(link -> createLinkMedia(link, stage, token)).collect(Collectors.toList());
    }

    private List<HarvestFileMediaStage> createHarvestFilesInStage(@NonNull List<Long> filesId, @NonNull Stage stage, @NonNull String token){
        return filesId.stream().map(fileId -> {
            try {
                return createHarvestFileMediaStage(fileId, stage, token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    private ScormMediaStage createScormMedia(String scormId, Stage stage, String token){
        return ScormMediaStage
                .builder()
                .scorm(scormService.find(scormId, token, false))
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

    private HarvestFileMediaStage createHarvestFileMediaStage(@NonNull Long fileId, @NonNull Stage stage, @NonNull String token) throws Exception {
        return HarvestFileMediaStage
                .builder()
                .harvestFile(harvestFileStorageService.getFileByIdAndAuthorizedClient(fileId.toString(), token, false))
                .stage(stage)
                .build();
    }
}
