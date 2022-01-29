package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.entities.AnswerQuestion;
import br.com.harvest.onboardexperience.domain.entities.QuestionEvent;
import br.com.harvest.onboardexperience.mappers.AnswerQuestionMapper;
import br.com.harvest.onboardexperience.mappers.QuestionEventMapper;
import br.com.harvest.onboardexperience.repositories.AnswerQuiestionRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnswerQuestionService {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private AnswerQuiestionRepository repository;


    public AnswerQuestionDto optionAnswer(AnswerQuestionDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        AnswerQuestion answerQuestion = AnswerQuestionMapper.INSTANCE.toEntity(dto);

        answerQuestion = repository.save(answerQuestion);

        log.info("The Question" + answerQuestion.getAnswer() + "was created sucessful");

        return AnswerQuestionMapper.INSTANCE.toDto(answerQuestion);
    }

    public AnswerQuestionDto answerQuestion(AnswerQuestionDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        AnswerQuestion answerQuestion = AnswerQuestionMapper.INSTANCE.toEntity(dto);

        answerQuestion = repository.save(answerQuestion);

        log.info("The Question" + answerQuestion.getAnswer() + "was created sucessful");

        return AnswerQuestionMapper.INSTANCE.toDto(answerQuestion);

    }
}
