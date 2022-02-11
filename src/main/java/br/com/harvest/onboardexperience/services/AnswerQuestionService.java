package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import br.com.harvest.onboardexperience.domain.entities.Question;
import br.com.harvest.onboardexperience.domain.exceptions.NotFoundException;
import br.com.harvest.onboardexperience.repositories.AnswerQuestionRepository;
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
public class AnswerQuestionService {

    @Autowired
    private AnswerQuestionRepository answerQuestionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    public AnswerQuestionDto answerQuestion(Long idQuestion, AnswerQuestionDto dto, String token) {
        this.questionService.findQuestionByIdAndFrom(idQuestion, null).getAnswersQuestions().forEach(
                answerQuestionDto -> {
                    if (answerQuestionDto.getAnswer().equalsIgnoreCase(dto.getAnswer()) && answerQuestionDto.getIsCorrect()) {
                        dto.setIsCorrect(true);
                    }
                }

        );

        return dto;
    }

    public AnswerQuestion findById(@NonNull Long id){
        return answerQuestionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Answer", "ID", id.toString())
        );
    }

    public List<AnswerQuestion> saveAll(@NonNull Question question, @NonNull List<AnswerQuestionDto> answers){
        return answerQuestionRepository.saveAll(answers.stream().map(answer -> answerDtoToAnswer(question, answer)).collect(Collectors.toList()));
    }

    public List<AnswerQuestion> updateAll(@NonNull Question question, @NonNull List<AnswerQuestionDto> answers){
        return answers.stream().map(answer -> updateAnswers(question, answer)).collect(Collectors.toList());
    }

    private AnswerQuestion updateAnswers(@NonNull Question question, @NonNull AnswerQuestionDto answerQuestionDto){

        if(Objects.isNull(answerQuestionDto.getId())){
            return answerQuestionRepository.save(answerDtoToAnswer(question, answerQuestionDto));
        }

        AnswerQuestion answer = findById(answerQuestionDto.getId());

        AnswerQuestion updatedQuestion = answerDtoToAnswer(question, answerQuestionDto);

        BeanUtils.copyProperties(updatedQuestion, answer, "id", "question");

        return answerQuestionRepository.save(answer);
    }

    public AnswerQuestion answerDtoToAnswer(Question question, AnswerQuestionDto dto) {
        return AnswerQuestion.
                builder()
                .answer(dto.getAnswer())
                .question(question)
                .isCorrect(dto.getIsCorrect())
                .build();
    }

    public void deleteById(Long idAnswer) {
        this.answerQuestionRepository.delete(findById(idAnswer));
    }

}