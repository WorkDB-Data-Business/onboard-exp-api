package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.CoinDto;
import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.domain.entities.Coin;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.domain.exceptions.StageNotFoundExecption;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.mappers.StageMapper;
import br.com.harvest.onboardexperience.repositories.StageRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class StageService {

    @Autowired
    JwtTokenUtils jwtUtils;

    @Autowired
    private StageRepository repository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private FetchService fetchService;

    //Consulta todas as etapas
    public Page<StageDto> findAllByTenant(Pageable pageable, @NotNull String token) {
        String tenant = jwtUtils.getUserTenant(token);
        return repository.findAllByClient_Tenant(tenant, pageable).map(StageMapper.INSTANCE::toDto);
    }

    //consulta etapa pelo Id
    public StageDto findByid(@NonNull Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        Stage stage = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new StageNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Stage", "ID", id.toString())));

        return StageMapper.INSTANCE.toDto(stage);
    }

    //Cria uma nova etapa.
    public StageDto create(@NonNull StageDto dto, MultipartFile file, String token) {
        String tenant = jwtUtils.getUserTenant(token);

        Stage stage = StageMapper.INSTANCE.toEntity(dto);
        setCoin(stage, dto, token);
        setClient(stage, token);
        stage = repository.save(stage);
        log.info("The Stage" + stage.getName() + "was saved sucessful");

        return StageMapper.INSTANCE.toDto(stage);
    }

    public void setCoin(Stage stage, StageDto dto, String token){
        stage.setCoin(fetchService.fetchCoin(dto.getCoinId(), token));
    }

    public void setClient(Stage stage, String token){
        stage.setClient(tenantService.fetchClientByTenantFromToken(token));
    }

    //Realiza alteração da etapa
    public StageDto update(@NonNull Long id, @NonNull StageDto dto, @NonNull MultipartFile file, @NonNull String token) {
        String tenant = jwtUtils.getUserTenant(token);

        Stage stage = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new StageNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("stage", "id", id.toString())));

        validate(stage,dto,tenant);

        BeanUtils.copyProperties(dto, stage, "id");

        setCoin(stage, dto, token);

        stage = repository.save(stage);

        log.info("The stage" + dto.getName()+"Was updated succesful");
        return StageMapper.INSTANCE.toDto(stage);
    }

    //Valida dados da etapa
    private void validate(@NonNull Stage stage, @NonNull StageDto dto, @NonNull final String tenant) {
        checkIfStageAlreadyExists(stage, dto, tenant);
    }

    //Verifica se a etapa existe
    private void checkIfStageAlreadyExists(@NonNull Stage stage, @NonNull StageDto dto, @NonNull final String tenant) {
        if (!checkIfIsSameStage(stage, dto)) {
            checkIfStageAlreadyExists(dto, tenant);
        }
    }

    // cria a verificar a etapa.
    private void checkIfStageAlreadyExists(StageDto dto, String tenant) {
        if (repository.findByNameAndClient_Tenant(dto.getName(), tenant).isPresent()) {
            throw new BusinessException(ExceptionMessageFactory.createAlreadyExistsMessage("stage", "name", dto.getName()));
        }
    }

    // tras o retorno da verificação da etapa
    private Boolean checkIfIsSameStage(@NonNull Stage stage, @NonNull StageDto stageDto) {
        Boolean sameName = stage.getName().equalsIgnoreCase(stageDto.getName());

        if (sameName) {
            return true;
        }
        return false;
    }

    // realiza exlcusao de uma etapa
    public void delete(@NonNull Long id,@NonNull String token) {
        String tenant = jwtUtils.getUserTenant(token);

        Stage stage = repository.findByIdAndClient_Tenant(id,tenant).orElseThrow(
                () -> new StageNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("stage", "ID",id.toString())));

        repository.delete(stage);
    }

    //Desativa um Etapa
    @Transactional
    public void disableStage(Long id, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        Stage stage = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new StageNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("stage","ID",id.toString())));
        stage.setIsActive(!stage.getIsActive());
        repository.save(stage);

        String isEnabled = stage.getIsActive().equals(true) ? "Disabled" : "enabled";
        log.info("The Stage with ID" + id + "Was" + isEnabled + "Sucessful");

    }
}
