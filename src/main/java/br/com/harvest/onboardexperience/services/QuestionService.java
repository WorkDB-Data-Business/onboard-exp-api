package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.domain.exceptions.EventNotFoundExecption;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.AnswerQuestionMapper;
import br.com.harvest.onboardexperience.mappers.QuestionMapper;
import br.com.harvest.onboardexperience.repositories.AnswerQuestionRepository;
import br.com.harvest.onboardexperience.repositories.QuestionRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService { //TODO: essa classe não precisa existir


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

    public QuestionDto createQuestion(QuestionDto dto, String token) {
        Question question = dtoToEntity(dto,token);
        QuestionDto questionEventFinal = QuestionMapper.INSTANCE.toDto(questionRepository.save(question));
        log.info("The Question" + question.getName() + " was created sucessful");
        questionEventFinal.getAnswers().forEach(
                answerQuestionDto -> {
                    AnswerQuestion answer = AnswerQuestionMapper.INSTANCE.toEntity(answerQuestionDto);
                    answer.setQuestion(question);
                    this.answerQuestionRepository.save(answer);
                }
        );
        return questionEventFinal;
    }

    private void setAuthor(Question question, String token) {
        question.setAuthor(userService.findUserById(jwtUtils.getUserId(token)));
    }

    public QuestionDto optionAnswer(QuestionDto dto, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        Question question = QuestionMapper.INSTANCE.toEntity(dto);

        setAuthor(question,token);

        question = questionRepository.save(question);

        log.info("The Question" + question.getName() + "was created sucessful");

        return QuestionMapper.INSTANCE.toDto(question);
    }

    public QuestionDto noteQuestion(QuestionDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        Question question = QuestionMapper.INSTANCE.toEntity(dto);

        setAuthor(question,token);

        question = questionRepository.save(question);

        return QuestionMapper.INSTANCE.toDto(question);

    }

    public Page<QuestionDto> findAll(String token, Pageable pageable) {
        User user = userRepository.findById(jwtUtils.getUserId(token)).orElseThrow(() -> new NotFoundException());
        return questionRepository.findAll(QuestionRepository.byAuthorizedClients(user.getClient()), pageable).map(QuestionMapper.INSTANCE::toDto);
    }

    public QuestionDto findById(Long id, String from) {
        if(Objects.nonNull(from)&&Objects.equals(from,"trilha")||Objects.equals(from,"biblioteca")){
        Question question = questionRepository.findById(id).orElseThrow(() -> new NotFoundException(("Não encontrado")));
        question.getAnswersQuestions().forEach(
                answerQuestion -> {
                    if(from.equalsIgnoreCase("trilha")){
                        answerQuestion.setIsCorrect(null);
                    }
                }
        );
            return QuestionMapper.INSTANCE.toDto(question);
        }
        throw new BusinessException("Erro no parametro from, não é igual aos permitidos(trilha/bilioeteca, e sim igual a "+from);
    }

    public AnswerQuestionDto optionAnswer(AnswerQuestionDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        AnswerQuestion answerQuestion = AnswerQuestionMapper.INSTANCE.toEntity(dto);

        answerQuestion = answerQuestionRepository.save(answerQuestion);

        log.info("The Question" + answerQuestion.getAnswer() + "was created sucessful");

        return AnswerQuestionMapper.INSTANCE.toDto(answerQuestion);
    }

    public AnswerQuestionDto answerQuestion(Long idQuestion,AnswerQuestionDto dto, String token) {

        this.findById(idQuestion,"biblioteca").getAnswers().forEach(
                answerQuestionDto -> {
                    if(answerQuestionDto.getAnswer().equalsIgnoreCase(dto.getAnswer()) && answerQuestionDto.getIsCorrect()){
                        dto.setIsCorrect(true);
                    }
                }
        );

        return dto;
    }

    public QuestionDto updateQuestionEvent (Long id, QuestionDto dto, String token) throws Exception {
        String tenant = jwtUtils.getUserTenant(token);
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

    private Question dtoToEntity(QuestionDto dto, String token) {

        return Question
                .builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .answersQuestions(dto.getAnswers().stream().map(AnswerQuestionMapper.INSTANCE::toEntity)
                        .collect(Collectors.toList()))
                .isActive(dto.getIsActive())
                .isMultipleChoice(dto.getAnswers().size() > 0)
                .scoreQuestion(dto.getNoteQuestion())
                .author(userService.findUserById(jwtUtils.getUserId(token)))
                .build();
    }

    public void delete(Long id, QuestionDto dto, String token) throws Exception {
        String tenant = jwtUtils.getUserTenant(token);

        Question question = findQuestionByIdAndToken(id, token);
        BeanUtils.copyProperties(dto, question, "id");

        questionRepository.delete(question);
    }
}

