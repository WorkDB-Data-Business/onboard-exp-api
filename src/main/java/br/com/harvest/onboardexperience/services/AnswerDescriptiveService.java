package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.AnswerDescriptiveDto;
import br.com.harvest.onboardexperience.domain.entities.AnswerDescriptive;
import br.com.harvest.onboardexperience.domain.exceptions.QuestionsExcpetion;
import br.com.harvest.onboardexperience.mappers.AnswerDescriptiveMapper;
import br.com.harvest.onboardexperience.mappers.QuestionMapper;
import br.com.harvest.onboardexperience.repositories.AnswerDescriptiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AnswerDescriptiveService {

    @Autowired
    private AnswerDescriptiveRepository answerDescriptiveRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    public AnswerDescriptiveDto answerDescriptive(Long idQuestion, AnswerDescriptiveDto dto, String token) {
        AnswerDescriptiveDto retorno = AnswerDescriptiveMapper.INSTANCE.toDto(answerDescriptiveRepository.save(dtoToEntity(idQuestion,dto,token)));
        retorno.setQuestionId(idQuestion);
        return retorno;
    }


    private AnswerDescriptive dtoToEntity(Long idQuestion,AnswerDescriptiveDto dto,String token){
        return AnswerDescriptive
                .builder()
                .id(dto.getId())
                .answerDescriptive(dto.getAnswerDescriptive())
                .author(userService.findUserByToken(token))
                .idQuestion(QuestionMapper.INSTANCE.toEntity(questionService.findQuestionById(idQuestion,null)))
                .build();

    }

    public Page<AnswerDescriptiveDto> findAllDescriptivesAnswers(Long idQuestion, Pageable pageable) {
        return this.answerDescriptiveRepository.findAll(AnswerDescriptiveRepository.byIDQuestion(idQuestion),pageable).map(AnswerDescriptiveMapper.INSTANCE::toDto);
    }

    public AnswerDescriptiveDto findById(Long idAnswerDescriptive) {
        AnswerDescriptive answerDescriptive =  this.answerDescriptiveRepository.findById(idAnswerDescriptive).orElseThrow(()
                -> new QuestionsExcpetion("Resposta descritiva não encontrada"));
        AnswerDescriptiveDto retorno = AnswerDescriptiveMapper.INSTANCE.toDto(answerDescriptive);
        retorno.setQuestionId(answerDescriptive.getIdQuestion().getId());
        return retorno;
    }

    public void deleteById(Long idAnswerDescriptive) {
        this.answerDescriptiveRepository.deleteById(idAnswerDescriptive);
    }

    public AnswerDescriptiveDto updateAnswerDescriptive(AnswerDescriptiveDto answerDescriptiveDto,String token) {
        if(Objects.nonNull(answerDescriptiveDto.getId())){
           AnswerDescriptive answerDescriptive = (this.answerDescriptiveRepository.saveAndFlush(dtoToEntity(answerDescriptiveDto.getQuestionId(),answerDescriptiveDto,token)));
           AnswerDescriptiveDto retorno = AnswerDescriptiveMapper.INSTANCE.toDto(answerDescriptive);
           retorno.setQuestionId(answerDescriptiveDto.getQuestionId());
           return retorno;
        }
        throw new QuestionsExcpetion("Não foi encontrado uma resposta descritiva para esse id");
    }
}
