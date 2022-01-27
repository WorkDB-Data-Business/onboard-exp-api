package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.EventDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.entities.Event;
import br.com.harvest.onboardexperience.domain.entities.QuestionEvent;
import br.com.harvest.onboardexperience.domain.exceptions.EventNotFoundExecption;
import br.com.harvest.onboardexperience.domain.exceptions.StageNotFoundExecption;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.EventMapper;
import br.com.harvest.onboardexperience.mappers.QuestionEventMapper;
import br.com.harvest.onboardexperience.repositories.EventRepository;
import br.com.harvest.onboardexperience.repositories.QuestionEventRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class QuestionEventService {


    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private QuestionEventRepository  questionEventRepository;


    public QuestionEventDto createQuestion(QuestionEventDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = QuestionEventMapper.INSTANCE.toEntity(dto);

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

    public QuestionEventDto answerQuestion(QuestionEventDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = QuestionEventMapper.INSTANCE.toEntity(dto);

        setClient(questionEvent,token);

        questionEvent = questionEventRepository.save(questionEvent);

        return QuestionEventMapper.INSTANCE.toDto(questionEvent);
    }


    public QuestionEventDto noteQuestion(QuestionEventDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = QuestionEventMapper.INSTANCE.toEntity(dto);

        setClient(questionEvent,token);

        questionEvent = questionEventRepository.save(questionEvent);

        return QuestionEventMapper.INSTANCE.toDto(questionEvent);

    }

    public QuestionEventDto Answercorrect(QuestionEventDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        QuestionEvent questionEvent = QuestionEventMapper.INSTANCE.toEntity(dto);

        setClient(questionEvent,token);

        questionEvent = questionEventRepository.save(questionEvent);

        return QuestionEventMapper.INSTANCE.toDto(questionEvent);

    }

}

