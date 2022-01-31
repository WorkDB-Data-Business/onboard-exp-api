package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.exceptions.BusinessException;
import br.com.harvest.onboardexperience.domain.exceptions.EventNotFoundExecption;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.AnswerQuestionMapper;
import br.com.harvest.onboardexperience.mappers.QuestionEventMapper;
import br.com.harvest.onboardexperience.repositories.AnswerQuiestionRepository;
import br.com.harvest.onboardexperience.repositories.QuestionEventRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
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


    //cria um pergunta no questionario
    @Transactional
    public QuestionEventDto createQuestion(QuestionEventDto dto, String token) {
        QuestionEvent questionEvent = dtoToEntity(dto,token);
        QuestionEventDto questionEventFinal = QuestionEventMapper.INSTANCE.toDto(questionEventRepository.save(questionEvent));
        log.info("The Question" + questionEvent.getName() + "was created sucessful");
        List<AnswerQuestion> respostas = new ArrayList<>();

        dto.getAnswers().forEach(
                answerQuestionDto -> {
                    respostas.add(AnswerQuestionMapper.INSTANCE.toEntity(answerQuestionDto));
                }
        );
        respostas.forEach(
                answerQuestion -> {
                    answerQuestion.setQuestionEvent(questionEvent);
                    this.repository.save(answerQuestion);
                }
        );
        return questionEventFinal;
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
                        answerQuestion.setIsCorrect(null);
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

    public AnswerQuestionDto answerQuestion(Long idQuestion,AnswerQuestionDto dto, String token) {

        this.findById(idQuestion,"biblioteca").getAnswers().forEach(
                answerQuestionDto -> {
                    if(answerQuestionDto.getAnswer().equalsIgnoreCase(dto.getAnswer())&&answerQuestionDto.getIsCorrect()){
                        dto.setIsCorrect(true);
                    }
                }
        );
        ;

        return dto;

    }

    public QuestionEventDto updateQuestionEvent (Long id, QuestionEventDto dto, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = questionEventRepository.findByIdAndClient_Tenant(id,token).orElseThrow(
                () ->  new EventNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Question", "Id", id.toString())));
        BeanUtils.copyProperties(dto,questionEvent, "id");
        questionEvent = questionEventRepository.save(questionEvent);
        log.info("The Question of event" + dto.getDescription()+ "Was update sucessful");
        return QuestionEventMapper.INSTANCE.toDto(questionEvent);
    }

    private QuestionEvent dtoToEntity(QuestionEventDto dto,String token) {

        return QuestionEvent
                .builder()
                .name(dto.getName())
                .descripton(dto.getDescription())
                .isActive(dto.getIsActive())
                .isMultipleChoice(dto.getAnswers().size() > 0)
                .noteQuestion(dto.getNoteQuestion())
                .client(tenantService.fetchClientByTenantFromToken(token))
                .build();
    }

    public void delete(Long id, QuestionEventDto dto, String token) {
        String tenant = jwtUtils.getUserTenant(token);

        QuestionEvent questionEvent = questionEventRepository.findByIdAndClient_Tenant(id,token).orElseThrow(
                () ->  new EventNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Question", "Id", id.toString())));
        BeanUtils.copyProperties(dto,questionEvent, "id");

        questionEventRepository.delete(questionEvent);
    }
}

