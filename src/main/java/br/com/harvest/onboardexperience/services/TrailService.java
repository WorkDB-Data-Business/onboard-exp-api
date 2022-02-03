package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.TrailSimpleDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.AlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.infra.storage.filters.CustomFilter;
import br.com.harvest.onboardexperience.infra.storage.services.AssetStorageService;
import br.com.harvest.onboardexperience.mappers.TrailMapper;
import br.com.harvest.onboardexperience.repositories.TrailRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
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
    private FetchService fetchService;

    public TrailDTO save(@NonNull TrailForm form, List<PositionForm> characterMapPositionPath,
                         MultipartFile mapImage, MultipartFile mapMusic, @NonNull String token) throws IOException {

        validate(form, token);

        Trail trail = formToTrail(form, characterMapPositionPath, token);

        uploadMapImage(trail, mapImage);

        uploadMapMusicPath(trail, mapMusic);

        trail = repository.save(trail);

        return TrailMapper.INSTANCE.toDto(trail);
    }

    public TrailDTO update(@NonNull Long id, @NonNull TrailForm form, List<PositionForm> characterMapPositionPath,
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

    public TrailDTO findTrailByIdAndEndUserByTokenAsColaborator(@NonNull Long id, @NonNull String token) {
        return repository.findOne(TrailRepository.byId(id).and(TrailRepository.byEndUser(userService.findUserByToken(token))))
                .map(TrailMapper.INSTANCE::toDto)
                .orElseThrow(() -> new NotFoundException("Trail", "ID", id.toString()));
    }

    public TrailDTO findTrailByIdAndEndUserByTokenAsAdmin(@NonNull Long id, @NonNull String token) {
        return repository.findOne(TrailRepository.byId(id).and(TrailRepository.byClient(tenantService.fetchClientByTenantFromToken(token))))
                .map(TrailMapper.INSTANCE::toDto)
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

    private Trail formToTrail(TrailForm form, List<PositionForm> characterMapPositionPath, @NonNull String token) {
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

}
