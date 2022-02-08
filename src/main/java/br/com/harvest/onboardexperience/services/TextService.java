package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.TextDto;
import br.com.harvest.onboardexperience.domain.entities.Text;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.TextNotFoundExecption;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.TextMapper;
import br.com.harvest.onboardexperience.repositories.TextRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TextService {


    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

//    private void setClient(Text text, String token) {
//        text.setAuthorizedClients((List<Client>) tenantService.fetchClientByTenantFromToken(token));
//    }

    public TextDto create(TextDto dto, String token) {

        Text text = TextMapper.INSTANCE.toEntity(dto);

        text.setAuthor(userService.findUserByToken(token));

        text = textRepository.save(text);

        log.info("The text" + text.getTitle() + "was created sucessful");

        return TextMapper.INSTANCE.toDto(text);

    }

//    public Page<TextDto> findALl(String token, Pageable pageable) {
//        User user = userRepository.findById(jwtUtils.getUserId(token)).orElseThrow(()-> new NotFoundException());
//        return textRepository.findByClient_Id(user.getClient().getId(),pageable).map(TextMapper.INSTANCE::toDto);
//
//    }

//    public Page<TextDto> findALl(String token, Pageable pageable) {
//        return textRepository.findAll(TextRepository.byAuthorizedClients(userService.findUserByToken(token).getClient()),
//                pageable).map(TextMapper.INSTANCE::toDto);
//    }

    public TextDto findById(@NonNull Long id) {
       Text text = textRepository.findById(id).orElseThrow(
                ()-> new TextNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Text", "id", id.toString())));
        return TextMapper.INSTANCE.toDto(text);

    }

//    public TextDto update(Long id, TextDto dto, String token) {
//
//        String tenant = jwtUtils.getUserTenant(token);
//
//        Text text = textRepository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
//                () -> new TextNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Text", "id", id.toString())));
//
//        validate(text,dto,token);
//
//        BeanUtils.copyProperties(dto,text, "id");
//
//        text = textRepository.save(text);
//
//        log.info("The Text" +dto.getTitle()+ "Was updated succesful");
//        return  TextMapper.INSTANCE.toDto(text);
//    }

//    @Operation(description = "Valida o texto")
//    private void validate(@NonNull Text text, @NonNull TextDto dto, @NonNull final String tenant) {
//        checkIfTextAlreadyExists(text, dto, tenant);
//    }

//    @Operation(description = "Verifica se o texto existe.")
//    private void checkIfTextAlreadyExists(@NonNull Text text, @NonNull TextDto dto, @NonNull final String tenant) {
//        if (!checkIfIsSameText(text, dto)) {
//            checkIfTextAlreadyExists(dto, tenant);
//        }
//    }

//    @Operation(description = "Tras o Retorno da verificação do texto.")
//    private Boolean checkIfIsSameText(@NonNull Text text, @NonNull TextDto textDto) {
//        Boolean sameName = text.getTitle().equalsIgnoreCase(textDto.getTitle());
//
//        if (sameName) {
//            return true;
//        }
//        return false;
//    }

//    @Operation(description = "Cria a verificaçao do texto.")
//    private void checkIfTextAlreadyExists(TextDto dto, String tenant) {
//        if (textRepository.findByIdTitleAnd_Tenant(dto.getTitle(),tenant).isPresent()) {
//            throw new BusinessException(ExceptionMessageFactory.createAlreadyExistsMessage("stage", "title", dto.getTitle()));
//        }
//    }

    public void delete(Long id) {
        Text text = textRepository.findById(id).orElseThrow(
                () -> new TextNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Text", "title", id.toString())));

        textRepository.delete(text);

    }

    public TextDto update(Long id, TextDto dto, String token) throws Exception {

        Text text = findtextByIdAndToken(id,token);
        BeanUtils.copyProperties(dto,text, "id");
        text = textRepository.save(text);
        log.info("The Text of event " + dto.getTitle()+ " was update sucessful");
        return  TextMapper.INSTANCE.toDto(text);

    }

    private Text findtextByIdAndToken(Long id, String token) throws Exception {
        User user = userService.findUserByToken(token);
        return textRepository
                .findOne(TextRepository.byId(id).and(TextRepository.byAuthorizedClients(user.getClient())))
                .or(()-> textRepository.findOne(TextRepository.byId(id).and(TextRepository.byAuthor(user))))
                .orElseThrow(
                        ()-> new Exception("Texto Not Found")
                );

    }

    public Page<TextDto> findALltext(String token, Pageable pageable) {
        String tenant = jwtUtils.getUserTenant(token);
        return textRepository.findAll(pageable).map(TextMapper.INSTANCE::toDto);

    }
}

