package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.QuestionnaireDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Questionnaire;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Transactional
    public QuestionnaireDto createQuestionnaire(QuestionnaireDto dto, String token) {
        Questionnaire questionnaire = this.questionnaireRepository.save(dtoToEntity(dto,token));

        questionnaire.setQuestionOfQuestionnaire(questionService.saveAll(dto.getQuestionOfQuestionnaire(), questionnaire, token));

        return entityToDto(questionnaire);
    }

    public QuestionnaireDto uploadImagePreview(@NonNull Long id, MultipartFile multipartFile){
        Questionnaire questionnaire = findQuestionnaireById(id);

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

        return QuestionnaireMapper.INSTANCE.toDto(questionnaire);
    }

    private Questionnaire dtoToEntity(QuestionnaireDto dto,String token){
        User author = userService.findUserById(jwtUtils.getUserId(token));
        return Questionnaire
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .isActive(dto.getIsActive())
                .author(author)
                .authorizedClients(fetchService.generateAuthorizedClients(dto.getAuthorizedClientsId(),author))
                .build();
    }

    public Page<QuestionnaireDto> findAllQuestionnaire(String token, Pageable pageable) {
            return questionnaireRepository.findAll(QuestionnaireRepository.byAuthorizedClients(userService.findUserByToken(token).getClient()),
                    pageable).map(QuestionnaireMapper.INSTANCE::toDto);
    }

    public QuestionnaireDto findQuestionnaireByIdAsDTO(@NonNull Long id, @NonNull String token) {
        return entityToDto(findQuestionnaireByAndToken(id, token));
    }

    private QuestionnaireDto entityToDto(Questionnaire questionnaire){
        QuestionnaireDto questionnaireDto = QuestionnaireMapper.INSTANCE.toDto(questionnaire);
        questionnaireDto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(questionnaire.getAuthorizedClients(), Client.class));
        return questionnaireDto;
    }

    public Questionnaire findQuestionnaireById(Long id) {
        return questionnaireRepository.findById(id).orElseThrow(()-> new NotFoundException(("Nao Encontrado.")));
    }

    public QuestionnaireDto update(@NonNull Long id, @NonNull QuestionnaireDto dto, @NonNull String token) throws Exception {
        Questionnaire questionnaire = findQuestionnaireByAndToken(id,token);

        Questionnaire updateQuestionnaire = dtoToEntity(dto,token);

        BeanUtils.copyProperties(updateQuestionnaire, questionnaire, "id", "author", "questionOfQuestionnaire");

        questionnaire = questionnaireRepository.save(questionnaire);

        questionnaire.setQuestionOfQuestionnaire(questionService.updateAll(dto.getQuestionOfQuestionnaire(), questionnaire, token));

        return entityToDto(questionnaire);
    }

    private Questionnaire findQuestionnaireByAndToken(@NonNull Long id, @NonNull String token) {
        User user = userService.findUserByToken(token);
        return  questionnaireRepository
                .findOne(QuestionnaireRepository.byId(id).and(QuestionnaireRepository.byAuthorizedClients(user.getClient())))
                .or(()-> questionnaireRepository.findOne(QuestionnaireRepository.byId(id).and(QuestionnaireRepository.byAuthor(user))))
                .orElseThrow(()-> new NotFoundException("Questionnaire", "ID", id.toString()));
    }

    public void deleteQuestionnaire(Long id, String token) {
        questionnaireRepository.delete(findQuestionnaireByAndToken(id,token));
    }
}

