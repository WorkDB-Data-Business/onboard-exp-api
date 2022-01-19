package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.TrailDTO;
import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrailForm;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import br.com.harvest.onboardexperience.mappers.TrailMapper;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.TrailRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TrailService {

    @Autowired
    private TrailRepository trilhaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtils;


    public Page<TrailDTO> searchAllTrals(Pageable pageable, String token) {

        return this.trilhaRepository.findAll(pageable).map(TrailMapper::toDto);

    }

    public TrailDTO saveTRail(MultipartFile file, TrailForm form, String token) throws IOException {
        UserDto userDTO = UserMapper.INSTANCE.toDto(userService.findUserById(tokenUtils.getUserId(token))) ;
        return TrailMapper.toDto(this.trilhaRepository.save(preencherTrilha(form,userDTO,file)));
    }

    private Trail preencherTrilha(TrailForm form, UserDto userDTO, MultipartFile file) throws IOException {

        return Trail
                .builder()
                .nameTrail(form.getNameTrail())
                .descriptionTrail(form.getDescricaoTrilha())
                .arquivoTrilhaNome(file.getOriginalFilename())
                .arquivoTrilhaBytes(file.getBytes())
                .userCreatedTrail(UserMapper.INSTANCE.toEntity(userDTO)).build();

    }

    public TrailDTO searchTrailId(Long idTrail) {

        return findTrilhaById(idTrail);
    }

    public TrailDTO findTrilhaById(Long id){
        return TrailMapper.toDto(this.trilhaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro ao buscar a trilha")));
    }

    public void deleteTrail(Long idTrail) {
        try{
            this.trilhaRepository.deleteById(idTrail);
        }catch (Exception e){
            System.out.println("Erro ao deletar"+e.getLocalizedMessage());
        }

    }
}
