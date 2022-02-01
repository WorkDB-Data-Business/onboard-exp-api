package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.enumerators.QuestionFrom;
import br.com.harvest.onboardexperience.mappers.AnswerQuestionMapper;
import br.com.harvest.onboardexperience.mappers.QuestionMapper;
import br.com.harvest.onboardexperience.repositories.AnswerQuestionRepository;
import br.com.harvest.onboardexperience.repositories.QuestionRepository;
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

import javax.ws.rs.NotFoundException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {


    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerQuestionRepository answerQuestionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FetchService fetchService;

    public QuestionDto createQuestion(QuestionDto dto, String token) {
        Question question = dtoToEntity(dto,token);
        QuestionDto questionEventFinal = QuestionMapper.INSTANCE.toDto(questionRepository.save(question));
        log.info("The Question" + question.getName() + " was created sucessful");
        questionEventFinal.getAnswersQuestions().forEach(
                answerQuestionDto -> {
                    AnswerQuestion answer = AnswerQuestionMapper.INSTANCE.toEntity(answerQuestionDto);
                    answer.setQuestion(question);
                    this.answerQuestionRepository.save(answer);
                }
        );
        questionEventFinal.setAuthorizedClientsId(GenericUtils.extractIDsFromList(question.getAuthorizedClients(), Client.class));
        return questionEventFinal;
    }

    private void setAuthor(Question question, String token) {
        question.setAuthor(userService.findUserByToken(token));
    }

    public AnswerQuestionDto putQuestionsOptions(Long idQuestion,AnswerQuestionDto dto, String token) throws Exception {
        return AnswerQuestionMapper.INSTANCE.toDto(this.answerQuestionRepository.save(AnswerQuestion.
                builder()
                .answer(dto.getAnswer())
                .question(findQuestionByIdAndToken(idQuestion,token))
                .author(userService.findUserById(jwtUtils.getUserId(token)))
                .isCorrect(dto.getIsCorrect())
                .build()));
    }


    public Page<QuestionDto> findAll(String token, Pageable pageable) {
        return questionRepository.findAll(QuestionRepository.byAuthorizedClients(userService.findUserByToken(token).getClient()), pageable).map(QuestionMapper.INSTANCE::toDto);
    }

    public QuestionDto findById(Long id, QuestionFrom from) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new NotFoundException(("Não encontrado")));
        question.getAnswersQuestions().forEach(
                answerQuestion -> {
                    if(QuestionFrom.TRAIL.equals(from)){
                        answerQuestion.setIsCorrect(null);
                    }
                }
        );
            return QuestionMapper.INSTANCE.toDto(question);
    }

    public AnswerQuestionDto answerQuestion(Long idQuestion,AnswerQuestionDto dto, String token) {
        this.findById(idQuestion,null).getAnswersQuestions().forEach(
                answerQuestionDto -> {
                    if(answerQuestionDto.getAnswer().equalsIgnoreCase(dto.getAnswer()) && answerQuestionDto.getIsCorrect()){
                        dto.setIsCorrect(true);
                    }
                }
        );
        return dto;
    }

    public QuestionDto updateQuestionEvent (Long id, QuestionDto dto, String token) throws Exception {
        Question question = findQuestionByIdAndToken(id, token);
        BeanUtils.copyProperties(dto, question, "id");
        question = questionRepository.save(question);
        log.info("The Question of event " + dto.getDescription()+ " was update sucessful");
        return QuestionMapper.INSTANCE.toDto(question);
    }

    private Question findQuestionByIdAndToken(@NonNull Long id, @NonNull String token) throws Exception {
        User user = userService.findUserByToken(token);
        return questionRepository
                .findOne(QuestionRepository.byId(id).and(QuestionRepository.byAuthorizedClients(user.getClient())))
                .or(() -> questionRepository.findOne(QuestionRepository.byId(id).and(QuestionRepository.byAuthor(user))))
                .orElseThrow(
                        () -> new Exception("Question not found")
                );
    }

    private Question dtoToEntity(QuestionDto dto,String token){
        User author = userService.findUserById(jwtUtils.getUserId(token));
        return Question
                .builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .answersQuestions(dto.getAnswersQuestions().stream().map(AnswerQuestionMapper.INSTANCE::toEntity)
                        .collect(Collectors.toList()))
                .isActive(dto.getIsActive())
                .isMultipleChoice(dto.getAnswersQuestions().size() > 0)
                .scoreQuestion(dto.getScoreQuestion())
                .author(author)
                .authorizedClients(fetchService.generateAuthorizedClients(dto.getAuthorizedClientsId(), author))
                .build();

    }

    public void delete(Long id, QuestionDto dto, String token) throws Exception {
        Question question = findQuestionByIdAndToken(id, token);
        BeanUtils.copyProperties(dto, question, "id");
        questionRepository.delete(question);
    }
}

