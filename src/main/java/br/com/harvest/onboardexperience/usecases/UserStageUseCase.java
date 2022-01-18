package br.com.harvest.onboardexperience.usecases;

import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.mappers.StageMapper;
import br.com.harvest.onboardexperience.repositories.StageRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserStageUseCase {

    @Autowired
    private FetchService fetchService;

    @Autowired
    private StageRepository repository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    // Deixa uma etapa disponivel para demais trilhas
    public List<StageDto> findAllStagesAvailables(@NonNull StageDto dto, MultipartFile file, String token) {
        String tenant = jwtUtils.getUserTenant(token);

        List<Stage> stages = repository.findAllByIsAvailableAndClient_Tenant(true, tenant);

        return stages.stream().map(StageMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

}
