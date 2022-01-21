package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.domain.entities.Stage;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import br.com.harvest.onboardexperience.mappers.StageMapper;
import br.com.harvest.onboardexperience.mappers.TrailMapper;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.StageRepository;
import br.com.harvest.onboardexperience.repositories.TrailRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrailService {

    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtils;

    @Autowired
    private  TenantService tenant;

    @Autowired
    private StageRepository stageRepository;

    public List<StageDto> findAllStagesAvailables(StageDto dto, MultipartFile file, String token) {

        String tenant = tokenUtils.getUserTenant(token);

        List<Stage> stages = stageRepository.findAllByIsAvailableAndClient_Tenant(true,tenant);

        return  stages.stream().map(StageMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    //Busca todas as trilhas.
    public Page<TrailDTO> searchAllTrals(Pageable pageable, String token) {

        return this.trailRepository.findAll(pageable).map(TrailMapper::toDto);

    }

    //Salva uma trilha no banco de dados.
    public TrailDTO saveTrail(MultipartFile file, TrailForm form, String token) throws IOException {
        UserDto userDTO = UserMapper.INSTANCE.toDto(userService.findUserById(tokenUtils.getUserId(token))) ;
        return TrailMapper.toDto(this.trailRepository.save(preencherTrilha(form,userDTO,file)));
    }

    // Preenche uma trilha.
    private Trail preencherTrilha(TrailForm form, UserDto userDTO, MultipartFile file) throws IOException {

        return Trail
                .builder()
                .nameTrail(form.getNameTrail())
                .descriptionTrail(form.getDescricaoTrilha())
                .arquivoTrilhaNome(file.getOriginalFilename())
                .arquivoTrilhaBytes(file.getBytes())
                .userCreatedTrail(UserMapper.INSTANCE.toEntity(userDTO)).build();

    }

    //busca uma tillha pelo ID.
    public TrailDTO searchTrailId(Long idTrail) {

        return findTrilhaById(idTrail);
    }

    public TrailDTO findTrilhaById(Long id){
        return TrailMapper.toDto(this.trailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro ao buscar a trilha")));
    }

    //deleta uma trilha do banco de dados.
    public void deleteTrail(Long idTrail) {
        try{
            this.trailRepository.deleteById(idTrail);
        }catch (Exception e){
            System.out.println("Erro ao deletar"+e.getLocalizedMessage());
        }

    }


}
