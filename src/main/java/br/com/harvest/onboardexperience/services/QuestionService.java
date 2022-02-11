package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.enumerators.QuestionFrom;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.mappers.QuestionMapper;

import br.com.harvest.onboardexperience.repositories.AnswerQuestionRepository;
import br.com.harvest.onboardexperience.repositories.QuestionRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public QuestionDto findQuestionByIdAndFrom(@NonNull Long id, QuestionFrom from) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new NotFoundException("Question", "ID", id.toString()));
        question.getAnswersQuestions().forEach(
                answerQuestion -> {
                    if(QuestionFrom.TRAIL.equals(from)){
                        answerQuestion.setIsCorrect(null);
                    }
                }
        );
        return QuestionMapper.INSTANCE.toDto(question);
    }

    public Question findQuestionById(@NonNull Long id){
        return questionRepository.findById(id).orElseThrow(() -> new NotFoundException("Question", "ID", id.toString()));
    }

    public List<Question> saveAll(@NonNull List<QuestionDto> questions, @NonNull Questionnaire questionnaire, @NonNull String token){
        return questions.stream().map(question -> saveQuestionAndAnswers(question, token, questionnaire)).collect(Collectors.toList());
    }

    public List<Question> updateAll(@NonNull List<QuestionDto> questions, @NonNull Questionnaire questionnaire, @NonNull String token){
        return questions.stream().map(question -> updateQuestionAndAnswers(question, questionnaire, token)).collect(Collectors.toList());
    }

    private Question updateQuestionAndAnswers(@NonNull QuestionDto questionDto, @NonNull Questionnaire questionnaire, @NonNull String token){

        if(Objects.isNull(questionDto.getId())){
            return saveQuestionAndAnswers(questionDto, token, questionnaire);
        }

        Question question = findQuestionById(questionDto.getId());

        Question updatedQuestion = dtoToEntity(questionDto, token, questionnaire);

        BeanUtils.copyProperties(updatedQuestion, question, "id", "author", "questionnaire", "answersQuestions");

        questionRepository.save(question);

        question.setAnswersQuestions(this.answerQuestionService.updateAll(question, questionDto.getAnswersQuestions()));

        return question;
    }

    private Question saveQuestionAndAnswers(@NonNull QuestionDto questionDto, @NonNull String token, @NonNull Questionnaire questionnaire){
        Question question = this.questionRepository.save(dtoToEntity(questionDto, token, questionnaire));
        question.setAnswersQuestions(this.answerQuestionService.saveAll(question, questionDto.getAnswersQuestions()));
        return question;
    }

    public Question findQuestionByIdAndToken(@NonNull Long id) throws Exception {
        return questionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Question", "ID", id.toString())
        );
    }

    private Question dtoToEntity(@NonNull QuestionDto dto, @NonNull String token, @NonNull Questionnaire questionnaire){
        return Question
                .builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .questionnaire(questionnaire)
                .isMultipleChoice(dto.getAnswersQuestions().size() > 0)
                .scoreQuestion(dto.getScoreQuestion())
                .build();

    }

    public void deleteQuestion(Long id) throws Exception {
        questionRepository.delete(findQuestionByIdAndToken(id));
    }
}

