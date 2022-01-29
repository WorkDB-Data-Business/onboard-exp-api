package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.QuestionEventDto;
import br.com.harvest.onboardexperience.domain.dtos.TextEventDto;
import br.com.harvest.onboardexperience.domain.entities.QuestionEvent;
import br.com.harvest.onboardexperience.domain.entities.TextEvent;
import br.com.harvest.onboardexperience.mappers.QuestionEventMapper;
import br.com.harvest.onboardexperience.mappers.TextEventMapper;
import br.com.harvest.onboardexperience.mappers.TrailMapper;
import br.com.harvest.onboardexperience.repositories.QuestionEventRepository;
import br.com.harvest.onboardexperience.repositories.TextEventRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TextEventService {


    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private TextEventRepository textEventRepository;



    private void setClient(TextEvent textEvent, String token) {
        textEvent.setClient(tenantService.fetchClientByTenantFromToken(token));
    }


    public TextEventDto createText(TextEventDto dto, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        TextEvent textEvent = TextEventMapper.INSTANCE.toEntity(dto);

        setClient(textEvent,token);

        textEvent = textEventRepository.save(textEvent);

        log.info("The text" + textEvent.getTitle() + "was created sucessful");

        return TextEventMapper.INSTANCE.toDto(textEvent);

    }


}

