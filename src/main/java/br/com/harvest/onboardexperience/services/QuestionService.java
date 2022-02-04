package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.AnswerDescriptiveDto;
import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.enumerators.QuestionFrom;
import br.com.harvest.onboardexperience.mappers.AnswerDescriptiveMapper;
import br.com.harvest.onboardexperience.mappers.AnswerQuestionMapper;
import br.com.harvest.onboardexperience.mappers.QuestionMapper;
import br.com.harvest.onboardexperience.repositories.AnswerDescriptiveRepository;
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
    private JwtTokenUtils jwtUtils;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AnswerQuestionService answerQuestionService;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private AnswerQuestionRepository answerQuestionRepository;

    public Page<QuestionDto> findAllQuestions(String token, Pageable pageable) {
        return questionRepository.findAll(QuestionRepository.byAuthorizedClients(userService.findUserByToken(token).getClient()), pageable).map(QuestionMapper.INSTANCE::toDto);
    }

    public QuestionDto findQuestionById(Long id, QuestionFrom from) {
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

    public QuestionDto createQuestion(QuestionDto dto, String token) {
        Question question = dtoToEntity(dto,token);
        QuestionDto questionEventFinal = QuestionMapper.INSTANCE.toDto(questionRepository.save(question));
        log.info("The Question" + question.getName() + " was created sucessful");
        this.answerQuestionService.saveAnswersQuestionsByQuestionCreating(questionEventFinal,question);
        questionEventFinal.setAuthorizedClientsId(GenericUtils.extractIDsFromList(question.getAuthorizedClients(), Client.class));
        return questionEventFinal;
    }

    public QuestionDto updateQuestionEvent (Long id, QuestionDto dto, String token) throws Exception {
        Question question = findQuestionByIdAndToken(id, token);

//        aaaa
        Question updatedQuestion = dtoToEntity(dto, token);

          Question finalQuestion = this.questionRepository.saveAndFlush(updatedQuestion);
        updatedQuestion.getAnswersQuestions().forEach(
                answerQuestion -> {
                    answerQuestion.setQuestion(finalQuestion);
                    this.answerQuestionRepository.saveAndFlush(answerQuestion);
                }
        );


        return QuestionMapper.INSTANCE.toDto(updatedQuestion);
    }

    public Question findQuestionByIdAndToken(@NonNull Long id, @NonNull String token) throws Exception {
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
                .id(dto.getId())
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

    public void deleteQuestion(Long id, String token) throws Exception {
        Question question = findQuestionByIdAndToken(id, token);
        questionRepository.deleteById(id);
    }

    private void setAuthor(Question question, String token) {
        question.setAuthor(userService.findUserByToken(token));
    }
}

