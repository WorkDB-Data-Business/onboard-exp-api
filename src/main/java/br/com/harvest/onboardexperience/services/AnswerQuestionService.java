package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionDto;
import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import br.com.harvest.onboardexperience.domain.entities.Question;
import br.com.harvest.onboardexperience.mappers.AnswerQuestionMapper;
import br.com.harvest.onboardexperience.repositories.AnswerQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerQuestionService {

    @Autowired
    private AnswerQuestionRepository answerQuestionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    public AnswerQuestionDto answerQuestion(Long idQuestion, AnswerQuestionDto dto, String token) {
        this.questionService.findQuestionById(idQuestion,null).getAnswersQuestions().forEach(
                answerQuestionDto -> {
                    if(answerQuestionDto.getAnswer().equalsIgnoreCase(dto.getAnswer()) && answerQuestionDto.getIsCorrect()){
                        dto.setIsCorrect(true);
                    }
                }
        );
        return dto;
    }

    public AnswerQuestionDto putQuestionsOptions(Long idQuestion,AnswerQuestionDto dto, String token) throws Exception {
        return AnswerQuestionMapper.INSTANCE.toDto(this.answerQuestionRepository.save(AnswerQuestion.
                builder()
                .answer(dto.getAnswer())
                .question(questionService.findQuestionByIdAndToken(idQuestion,token))
                .isCorrect(dto.getIsCorrect())
                .build()));
    }


    public void saveAnswersQuestionsByQuestionCreating(QuestionDto questionEventFinal, Question question) {
        questionEventFinal.getAnswersQuestions().forEach(
                answerQuestionDto -> {
                    AnswerQuestion answer = AnswerQuestionMapper.INSTANCE.toEntity(answerQuestionDto);
                    answer.setQuestion(question);
                    this.answerQuestionRepository.save(answer);
                }
        );
    }

    public void deleteById(Long idAnswer) {
        this.answerQuestionRepository.deleteById(idAnswer);
    }
}
