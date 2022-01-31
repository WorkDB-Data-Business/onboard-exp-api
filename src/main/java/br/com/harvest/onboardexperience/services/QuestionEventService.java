package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.EventDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.AnswerQuestionFormDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.QuestionEventFormDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.domain.exceptions.EventNotFoundExecption;
import br.com.harvest.onboardexperience.domain.exceptions.StageNotFoundExecption;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.AnswerQuestionMapper;
import br.com.harvest.onboardexperience.mappers.EventMapper;
import br.com.harvest.onboardexperience.mappers.QuestionEventMapper;
import br.com.harvest.onboardexperience.repositories.AnswerQuiestionRepository;
import br.com.harvest.onboardexperience.repositories.EventRepository;
import br.com.harvest.onboardexperience.repositories.QuestionEventRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionEventService { //TODO: essa classe não precisa existir


    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private QuestionEventRepository  questionEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerQuiestionRepository repository;


    @Transactional
    public QuestionEventDto createQuestion(QuestionEventFormDto dto, String token) {

        QuestionEvent questionEvent = quenstionFormToQuestionEntity(dto);

        setClient(questionEvent,token);

        questionEvent = questionEventRepository.save(questionEvent);

        log.info("The Question" + questionEvent.getName() + "was created sucessful");

        return QuestionEventMapper.INSTANCE.toDto(questionEvent);
    }

    private void setClient(QuestionEvent questionEvent, String token) {
        questionEvent.setClient(tenantService.fetchClientByTenantFromToken(token));
    }


    public QuestionEventDto optionAnswer(QuestionEventDto dto, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = QuestionEventMapper.INSTANCE.toEntity(dto);

        setClient(questionEvent,token);

        questionEvent = questionEventRepository.save(questionEvent);

        log.info("The Question" + questionEvent.getName() + "was created sucessful");

        return QuestionEventMapper.INSTANCE.toDto(questionEvent);
    }

    public QuestionEventDto noteQuestion(QuestionEventDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = QuestionEventMapper.INSTANCE.toEntity(dto);

        setClient(questionEvent,token);

        questionEvent = questionEventRepository.save(questionEvent);

        return QuestionEventMapper.INSTANCE.toDto(questionEvent);

    }

    private QuestionEvent quenstionFormToQuestionEntity(QuestionEventFormDto formDto){
        var question = new QuestionEvent();
        question.setId(null);
        question.setName(formDto.getName());
        question.setDescripton(formDto.getDescripton());
        question.setNoteQuestion(formDto.getNoteQuestion());
        question.setIsActive(true);

        if(formDto.getAnswares() != null && formDto.getAnswares().stream().count() > 0){
            question.setIsMultipleChoice(true);
            for(AnswerQuestionFormDto ans : formDto.getAnswares()){
                AnswerQuestion answare = new AnswerQuestion(null, ans.getAnswer(), question, ans.getIscorrect());
                question.getAnswers().add(answare);
            }
        }else{
            question.setIsMultipleChoice(false);
        }

        return question;
    }

    public Page<QuestionEventDto> findAll(String token, Pageable pageable) {
        User user = userRepository.findById(jwtUtils.getUserId(token)).orElseThrow(() -> new NotFoundException());
        return questionEventRepository.findByClient_Id(user.getClient().getId(), pageable).map(QuestionEventMapper.INSTANCE::toDto);
    }

    public QuestionEventDto findById(Long id,String from) {
        if(Objects.nonNull(from)&&Objects.equals(from,"trilha")||Objects.equals(from,"biblioteca")){
        QuestionEvent question = questionEventRepository.findById(id).orElseThrow(() -> new NotFoundException(("Não encontrado")));
        question.getAnswers().forEach(
                answerQuestion -> {
                    if(from.equalsIgnoreCase("trilha")){
                        answerQuestion.setIscorrect(null);
                    }
                }
        );
            return QuestionEventMapper.INSTANCE.toDto(question);
        }
        throw new BusinessException("Erro no parametro from, não é igual aos permitidos(trilha/bilioeteca, e sim igual a "+from);
    }

    public AnswerQuestionDto optionAnswer(AnswerQuestionDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        AnswerQuestion answerQuestion = AnswerQuestionMapper.INSTANCE.toEntity(dto);

        answerQuestion = repository.save(answerQuestion);

        log.info("The Question" + answerQuestion.getAnswer() + "was created sucessful");

        return AnswerQuestionMapper.INSTANCE.toDto(answerQuestion);
    }

    public AnswerQuestionDto answerQuestion(AnswerQuestionDto dto, String token) {

        this.findById(dto.getIdQuestion(),"biblioteca").getAnswers().forEach(
                answerQuestionDto -> {
                    if(dto.getAnswer().equalsIgnoreCase(answerQuestionDto.getAnswer())){
                        dto.setIsCorrect(true);
                    }
                }
        );

        String tenant = jwtUtils.getUserTenant(token);
        AnswerQuestion answerQuestion = AnswerQuestionMapper.INSTANCE.toEntity(dto);

        answerQuestion = repository.save(answerQuestion);

        log.info("The Question" + answerQuestion.getAnswer() + "was created sucessful");

        return AnswerQuestionMapper.INSTANCE.toDto(answerQuestion);

    }

    public QuestionEventDto updateQuestionEvent (Long id, QuestionEventFormDto dto, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = questionEventRepository.findByIdAndClient_Tenant(id,token).orElseThrow(
                () ->  new EventNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Question", "Id", id.toString())));
        BeanUtils.copyProperties(dto,questionEvent, "id");
        questionEvent = questionEventRepository.save(questionEvent);
        log.info("The Question of event" + dto.getDescripton()+ "Was update sucessful");
        return QuestionEventMapper.INSTANCE.toDto(questionEvent);
    }


    public void delete(Long id, QuestionEventFormDto dto, String token) {
        String tenant = jwtUtils.getUserTenant(token);

        QuestionEvent questionEvent = questionEventRepository.findByIdAndClient_Tenant(id,token).orElseThrow(
                () ->  new EventNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Question", "Id", id.toString())));
        BeanUtils.copyProperties(dto,questionEvent, "id");

        questionEventRepository.delete(questionEvent);
    }
}

