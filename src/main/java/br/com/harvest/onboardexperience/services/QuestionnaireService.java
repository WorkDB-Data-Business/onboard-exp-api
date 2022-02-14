package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.QuestionnaireDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionnaireSimpleDTO;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Questionnaire;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.AlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.services.AssetStorageService;

import br.com.harvest.onboardexperience.mappers.QuestionnaireMapper;
import br.com.harvest.onboardexperience.repositories.QuestionnaireRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.util.Objects;

@Slf4j
@Service
public class QuestionnaireService {

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerQuestionService answerQuestionService;

    @Autowired
    private AssetStorageService assetStorageService;

    public QuestionnaireDto create(@NonNull QuestionnaireDto dto, @NonNull String token) {

        Questionnaire questionnaire = dtoToEntity(dto,token);

        validateIfAlreadyExists(questionnaire);

        questionnaire = this.questionnaireRepository.save(questionnaire);

        questionnaire.setQuestionOfQuestionnaire(questionService.saveAll(dto.getQuestionOfQuestionnaire(), questionnaire, token));

        return entityToDto(questionnaire);
    }

    private void validateIfAlreadyExists(Questionnaire questionnaire, @NonNull QuestionnaireDto form) {
        if(!questionnaire.getName().equalsIgnoreCase(form.getName())){
            validateIfAlreadyExists(questionnaire);
        }
    }

    private void validateIfAlreadyExists(@NonNull Questionnaire questionnaire) {
        if(questionnaireRepository.existsByNameAndAuthor_Client(questionnaire.getName(), questionnaire.getAuthor().getClient())){
            throw new AlreadyExistsException("Questionnaire", "name", questionnaire.getName());
        }
    }

    public QuestionnaireDto uploadImagePreview(@NonNull Long id, MultipartFile multipartFile, @NonNull String token){
        Questionnaire questionnaire = findByIdAndAuthorizedClients(id, token, true);

        if(Objects.nonNull(multipartFile)){
            questionnaire.setPreviewImagePath(assetStorageService.uploadAsset(
                    multipartFile,
                    questionnaire.getAuthor().getClient().getCnpj(),
                    questionnaire.getName(),
                    FileTypeEnum.ASSET,
                    questionnaire.getAuthor()
            ));
            questionnaireRepository.save(questionnaire);
        }

        return entityToDto(questionnaire);
    }

    private Questionnaire dtoToEntity(@NonNull QuestionnaireDto dto, @NonNull String token){
        User author = userService.findUserById(jwtUtils.getUserId(token));
        return Questionnaire
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .isActive(dto.getIsActive())
                .minimumScore(dto.getMinimumScore())
                .author(author)
                .authorizedClients(fetchService.generateAuthorizedClients(dto.getAuthorizedClientsId(),author))
                .build();
    }

    public Page<QuestionnaireSimpleDTO> findAllQuestionnaire(@NonNull String token, Pageable pageable) {
            return questionnaireRepository.findAll(QuestionnaireRepository.byAuthorizedClients(userService.findUserByToken(token).getClient()),
                    pageable).map(this::entityToSimpleDto);
    }

    public QuestionnaireDto findQuestionnaireByIdAsDTO(@NonNull Long id, @NonNull String token) {
        return entityToDto(findByIdAndAuthorizedClients(id, token, false));
    }

    private QuestionnaireDto entityToDto(@NonNull Questionnaire questionnaire){
        QuestionnaireDto questionnaireDto = QuestionnaireMapper.INSTANCE.toDto(questionnaire);
        questionnaireDto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(questionnaire.getAuthorizedClients(), Client.class));
        questionnaireDto.setStorage(Storage.QUIZZ);
        return questionnaireDto;
    }

    private QuestionnaireSimpleDTO entityToSimpleDto(@NonNull Questionnaire questionnaire){
        QuestionnaireSimpleDTO questionnaireDto = QuestionnaireMapper.INSTANCE.toSimpleDto(questionnaire);
        questionnaireDto.setStorage(Storage.QUIZZ);
        return questionnaireDto;
    }

    public Questionnaire findByIdAndAuthorizedClients(@NonNull Long id, @NonNull String token, Boolean validateAuthor){
        User user = userService.findUserByToken(token);

        Questionnaire questionnaire = questionnaireRepository
                .findOne(QuestionnaireRepository.byId(id).and(QuestionnaireRepository.byAuthorizedClients(user.getClient())))
                .or(() -> questionnaireRepository.findOne(QuestionnaireRepository.byId(id).and(QuestionnaireRepository.byAuthor(user))))
                .orElseThrow(
                        () -> new NotFoundException("Questionnaire", "ID", id.toString())
                );

        if(validateAuthor){
            StorageService.validateAuthor(questionnaire, Questionnaire.class, user, "You're not the author of the questionnaire.");
        }

        return questionnaire;
    }

    public QuestionnaireDto update(@NonNull Long id, @NonNull QuestionnaireDto dto, @NonNull String token) throws Exception {
        Questionnaire questionnaire = findByIdAndAuthorizedClients(id,token, true);

        validateIfAlreadyExists(questionnaire, dto);

        Questionnaire updateQuestionnaire = dtoToEntity(dto,token);


        BeanUtils.copyProperties(updateQuestionnaire, questionnaire, "id", "author", "questionOfQuestionnaire");

        questionnaire = questionnaireRepository.save(questionnaire);

        questionnaire.setQuestionOfQuestionnaire(questionService.updateAll(dto.getQuestionOfQuestionnaire(), questionnaire, token));

        return entityToDto(questionnaire);
    }

    public void deleteQuestionnaire(@NonNull Long id, @NonNull String token) {
        questionnaireRepository.delete(findByIdAndAuthorizedClients(id, token, true));
    }
}

