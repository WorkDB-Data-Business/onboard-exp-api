package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.forms.PositionForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.AlreadyExistsException;
import br.com.harvest.onboardexperience.infra.storage.services.AssetStorageService;
import br.com.harvest.onboardexperience.mappers.TrailMapper;
import br.com.harvest.onboardexperience.repositories.TrailRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private TenantService tenant;

    @Autowired
    private PositionService positionService;

    @Autowired
    private AssetStorageService assetStorageService;

    @Autowired
    private FetchService fetchService;

    public TrailDTO save(@NonNull TrailForm form, List<PositionForm> characterMapPositionPath, @NonNull MultipartFile mapImage, MultipartFile mapMusic, @NonNull String token) throws IOException {

        validate(form);

        Trail trail = formToTrail(form, characterMapPositionPath);

        setAuthor(trail, token);
        setCoin(trail, form, token);

        uploadMapImage(trail, mapImage);

        uploadMapMusicPath(trail, mapMusic);

        trail = repository.save(trail);

        return TrailMapper.INSTANCE.toDto(trail);
    }

    private void validate(TrailForm form){
        validateIfAlreadyExistsByName(form.getName());
    }

    private void setAuthor(@NonNull Trail trail, @NonNull String token){
        trail.setAuthor(userService.findUserByToken(token));
    }

    private void setCoin(@NonNull Trail trail, @NonNull TrailForm form, @NonNull String token){
        trail.setCoin(fetchService.fetchCoin(form.getCoinId(), token));
    }

    private void validate(TrailForm form, Trail trail){
        if(trail.getName().equalsIgnoreCase(form.getName())){
            validateIfAlreadyExistsByName(form.getName());
        }
    }

    private void validateIfAlreadyExistsByName(String name){
        if(repository.existsByName(name)){
            throw new AlreadyExistsException("Trail", "name", name);
        }
    }

    private Trail formToTrail(TrailForm form, List<PositionForm> characterMapPositionPath) {
        return Trail
                .builder()
                .name(form.getName())
                .description(form.getDescription())
                .conclusionDate(form.getConclusionDate())
                .isActive(form.getIsActive())
                .characterMapPositionPath(positionService.getPosition(characterMapPositionPath))
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
            trail.setMapImagePath(
                    assetStorageService.uploadAsset(
                            mapMusicPath,
                            trail.getAuthor().getClient().getCnpj(),
                            MessageFormat.format("{0}_music", trail.getName()),
                            FileTypeEnum.ASSET, trail.getAuthor()
                    )
            );
        }
    }

    //busca uma tillha pelo ID.
    public TrailDTO searchTrailId(Long idTrail) {

        return findTrilhaById(idTrail);
    }

    public TrailDTO findTrilhaById(Long id){
        return TrailMapper.INSTANCE.toDto(this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro ao buscar a trilha")));
    }

    //deleta uma trilha do banco de dados.
    public void deleteTrail(Long idTrail) {
        try{
            this.repository.deleteById(idTrail);
        }catch (Exception e){
            System.out.println("Erro ao deletar"+e.getLocalizedMessage());
        }

    }


}
