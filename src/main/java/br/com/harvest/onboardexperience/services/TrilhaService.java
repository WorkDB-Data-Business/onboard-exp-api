package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.TrilhaDTO;
import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.TrilhaForm;
import br.com.harvest.onboardexperience.domain.entities.Trilha;
import br.com.harvest.onboardexperience.mappers.TrilhaMapper;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.TrilhaRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TrilhaService {

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtils;


    public Page<TrilhaDTO> buscarTodasTrilhas(Pageable pageable, String token) {

        return this.trilhaRepository.findAll(pageable).map(TrilhaMapper::toDto);

    }

    public TrilhaDTO salvarTrilhaNova(MultipartFile file, TrilhaForm form, String token) throws IOException {
        UserDto userDTO = UserMapper.INSTANCE.toDto(userService.findUserById(tokenUtils.getUserId(token))) ;
        return TrilhaMapper.toDto(this.trilhaRepository.save(preencherTrilha(form,userDTO,file)));
    }

    private Trilha preencherTrilha(TrilhaForm form, UserDto userDTO, MultipartFile file) throws IOException {

        return Trilha
                .builder()
                .nomeTrilha(form.getNomeTrilha())
                .descricaoTrilha(form.getDescricaoTrilha())
                .arquivoTrilhaNome(file.getOriginalFilename())
                .arquivoTrilhaBytes(file.getBytes())
                .userCreatedTrilha(UserMapper.INSTANCE.toEntity(userDTO)).build();

    }

    public TrilhaDTO buscarTrilhaPorId(Long idTrilha) {

        return findTrilhaById(idTrilha);
    }

    public TrilhaDTO findTrilhaById(Long id){
        return TrilhaMapper.toDto(this.trilhaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro ao buscar a trilha")));
    }

    public void deletarTrilha(Long idTrilha) {
        try{
            this.trilhaRepository.deleteById(idTrilha);
        }catch (Exception e){
            System.out.println("Erro ao deletar"+e.getLocalizedMessage());
        }

    }
}
