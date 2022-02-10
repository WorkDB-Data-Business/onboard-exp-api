package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.TrailSimpleDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.entities.keys.UserTrailRegistrationId;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.AlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.infra.storage.filters.CustomFilter;
import br.com.harvest.onboardexperience.infra.storage.services.AssetStorageService;
import br.com.harvest.onboardexperience.mappers.TrailMapper;
import br.com.harvest.onboardexperience.repositories.StageUserRepository;
import br.com.harvest.onboardexperience.repositories.TrailRepository;
import br.com.harvest.onboardexperience.repositories.UserTrailRegistrationRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class TrailService {

    @Autowired
    private TrailRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtils;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private AssetStorageService assetStorageService;

    @Autowired
    private UserTrailRegistrationRepository userTrailRegistrationRepository;

    @Autowired
    private StageUserRepository stageUserRepository;

    @Autowired
    private FetchService fetchService;

    public List<Trail> findAll(){
        return repository.findAll();
    }

    public List<Trail> findAllByClient(@NonNull Client client){
        return repository.findAll(TrailRepository.byClient(client));
    }

    public TrailDTO save(@NonNull TrailForm form, List<PositionDTO> characterMapPositionPath,
                         MultipartFile mapImage, MultipartFile mapMusic, @NonNull String token) throws IOException {

        validate(form, token);

        Trail trail = formToTrail(form, characterMapPositionPath, token);

        uploadMapImage(trail, mapImage);

        uploadMapMusicPath(trail, mapMusic);

        trail = repository.save(trail);

        return TrailMapper.INSTANCE.toDto(trail);
    }

    public TrailDTO update(@NonNull Long id, @NonNull TrailForm form, List<PositionDTO> characterMapPositionPath,
                         MultipartFile mapImage, MultipartFile mapMusic, @NonNull String token) throws IOException {


        Trail updatedTrail = formToTrail(form, characterMapPositionPath, token);

        Trail trail = findTrailByIdAndToken(id, token);

        validate(form, updatedTrail);

        uploadMapImage(updatedTrail, mapImage);

        uploadMapMusicPath(updatedTrail, mapMusic);

        BeanUtils.copyProperties(updatedTrail, trail,
                "id", "author", "client", "createdBy", "createdAt",
                !Objects.nonNull(mapImage) ? "mapImagePath" : "",
                !Objects.nonNull(mapMusic) ? "mapMusicPath" : "");

        return TrailMapper.INSTANCE.toDto(repository.save(trail));
    }

    public void delete(@NonNull Long id, @NonNull String token){
        repository.delete(findTrailByIdAndToken(id, token));
    }

    public Page<TrailSimpleDTO> findAll(Pageable pageable, CustomFilter filter, @NonNull String token){
        return repository.findAll(createQuery(filter, token), pageable).map(TrailMapper.INSTANCE::toSimpleDto);
    }

    public Page<TrailSimpleDTO> findAllMyTrails(Pageable pageable, CustomFilter filter, @NonNull String token){
        return repository.findAll(createQuery(filter, token)
                        .and(TrailRepository.byEndUser(userService.findUserByToken(token))),
                pageable).map(TrailMapper.INSTANCE::toSimpleDto);
    }

    private Specification<Trail> createQuery(@NonNull CustomFilter filter, @NonNull String token){
        Specification<Trail> query = Specification.where(TrailRepository.byClient(tenantService.fetchClientByTenantFromToken(token)));

        if(StringUtils.hasText(filter.getCustomFilter())){
            query = query.and(TrailRepository.byCustomFilter(filter.getCustomFilter()));
        }

        return query;
    }

    public TrailDTO findTrailByIdAndEndUserByTokenAsDTOAsColaborator(@NonNull Long id, @NonNull String token) {
        return TrailMapper.INSTANCE.toDto(findTrailByIdAndEndUserByTokenAsColaborator(id, token));
    }

    public TrailDTO findTrailByIdAndTokenAsDTOAsAdmin(@NonNull Long id, @NonNull String token) {
        return TrailMapper.INSTANCE.toDto(findTrailByIdAndTokenAsAdmin(id, token));
    }

    public Trail findTrailByIdAndEndUserByTokenAsColaborator(@NonNull Long id, @NonNull String token) {
        return repository.findOne(TrailRepository.byId(id).and(TrailRepository.byEndUser(userService.findUserByToken(token))))
                .orElseThrow(() -> new NotFoundException("Trail", "ID", id.toString()));
    }

    public Trail findTrailByIdAndTokenAsAdmin(@NonNull Long id, @NonNull String token) {
        return repository.findOne(TrailRepository.byId(id).and(TrailRepository.byClient(tenantService.fetchClientByTenantFromToken(token))))
                .orElseThrow(() -> new NotFoundException("Trail", "ID", id.toString()));
    }

    private void validate(TrailForm form, @NonNull String token){
        validateIfAlreadyExistsByNameAndClient(form.getName(), tenantService.fetchClientByTenantFromToken(token));
    }

    private void validate(TrailForm form, Trail trail){
        if(!trail.getName().equalsIgnoreCase(form.getName())){
            validateIfAlreadyExistsByNameAndClient(form.getName(), trail.getClient());
        }
    }

    private void validateIfAlreadyExistsByNameAndClient(String name, Client client){
        if(repository.existsByNameAndClient(name, client)){
            throw new AlreadyExistsException("Trail", "name", name);
        }
    }

    private Trail formToTrail(TrailForm form, List<PositionDTO> characterMapPositionPath, @NonNull String token) {
        return Trail
                .builder()
                .name(form.getName())
                .description(form.getDescription())
                .conclusionDate(form.getConclusionDate())
                .coin(fetchService.fetchCoin(form.getCoinId(), token))
                .isActive(form.getIsActive())
                .author(userService.findUserByToken(token))
                .groups(fetchService.fetchGroups(form.getGroupsId(), token))
                .characterMapPositionPath(positionService.getPosition(characterMapPositionPath))
                .client(tenantService.fetchClientByTenantFromToken(token))
                .build();
    }

    private void uploadMapImage(Trail trail, MultipartFile mapImage){
        if(Objects.nonNull(trail) && Objects.nonNull(mapImage)){
            trail.setMapImagePath(
                    assetStorageService.uploadAsset(
                            mapImage,
                            trail.getAuthor().getClient().getCnpj(),
                            MessageFormat.format("{0}_map", trail.getName()),
                            FileTypeEnum.ASSET, trail.getAuthor()
                    )
            );
        }
    }

    private void uploadMapMusicPath(Trail trail, MultipartFile mapMusicPath){
        if(Objects.nonNull(trail) && Objects.nonNull(mapMusicPath)){
            trail.setMapMusicPath(
                    assetStorageService.uploadAsset(
                            mapMusicPath,
                            trail.getAuthor().getClient().getCnpj(),
                            MessageFormat.format("{0}_music", trail.getName()),
                            FileTypeEnum.ASSET, trail.getAuthor()
                    )
            );
        }
    }

    public Trail findTrailByIdAndToken(@NonNull Long id, @NonNull String token){
        return this.repository.findByIdAndClient(id, tenantService.fetchClientByTenantFromToken(token))
                .orElseThrow(() -> new NotFoundException("Trail", "ID", id.toString()));
    }

    public void startTrail(@NonNull Long id, @NonNull String token){
        Trail trail = findTrailByIdAndEndUserByTokenAsColaborator(id, token);
        User user = userService.findUserByToken(token);

        if(LocalDateTime.now().isAfter(trail.getConclusionDate())){
            throw new BusinessException("You cannot start a trail after its conclusion date.");
        }

        if(!userTrailRegistrationRepository.existsById(createUserRegistrationId(trail.getId(), user.getId()))){
            userTrailRegistrationRepository.save(createRegistration(trail, user));
        }
    }

    public void finishTrail(@NonNull Long id, @NonNull String token) throws Exception {

        User user = userService.findUserByToken(token);
        Trail trail = findTrailByIdAndToken(id, token);

        UserTrailRegistration registration =
                userTrailRegistrationRepository.findById(createUserRegistrationId(id, user.getId()))
                        .orElseThrow(() -> new Exception("The user haven't started the trail yet."));

        List<StageUser> userStages = stageUserRepository.findAll(StageUserRepository.byUserAndTrail(user, trail));

        boolean isAllStagesFinished = !ObjectUtils.isEmpty(userStages) && userStages.stream().allMatch(StageUser::getIsCompleted);

        if(isAllStagesFinished && Objects.isNull(registration.getFinishedTrailDate())){
            registration.setAverageScore(calculateAverageScoreOnTrail(userStages));
            registration.setFinishedTrailDate(LocalDateTime.now());
            userTrailRegistrationRepository.save(registration);
        }
    }

    private BigDecimal calculateAverageScoreOnTrail(List<StageUser> userStages){
        if(!ObjectUtils.isEmpty(userStages)){
            return userStages.stream().map(StageUser::getScore).reduce(BigDecimal.ZERO,
                    BigDecimal::add).divide(BigDecimal.valueOf(userStages.size()), 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public void validateIfUserStartedTheTrail(@NonNull Long id, @NonNull String token){
        if(!userTrailRegistrationRepository.existsById(createUserRegistrationId(id, userService.findUserByToken(token).getId()))){
            throw new BusinessException("The user haven't started the trail yet.");
        }
    }

    private UserTrailRegistrationId createUserRegistrationId(@NonNull Long trailId, @NonNull Long userId){
        return UserTrailRegistrationId.builder().trail(trailId).user(userId).build();
    }

    public UserTrailRegistration createRegistration(@NonNull Trail trail, @NonNull User user){
        return UserTrailRegistration.builder()
                .trail(trail)
                .user(user)
                .startedTrailDate(LocalDateTime.now())
                .build();
    }

}
