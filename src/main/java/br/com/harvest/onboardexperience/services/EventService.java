package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.dtos.EventDto;
import br.com.harvest.onboardexperience.domain.entities.Event;
import br.com.harvest.onboardexperience.domain.exceptions.EventNotFoundExecption;
import br.com.harvest.onboardexperience.domain.exceptions.StageNotFoundExecption;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.EventMapper;
import br.com.harvest.onboardexperience.repositories.EventRepository;
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
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;


    public Page<EventDto> findAllByTenant(Pageable pageable, String token) {

        String tenant = jwtUtils.getUserTenant(token);
        return repository.findAllByClient_Tenant(tenant, pageable).map(EventMapper.INSTANCE::toDto);
    }

    public EventDto searchEventId(@NonNull Long id,@NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        Event event = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new StageNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Event", "ID", id.toString())));

        return EventMapper.INSTANCE.toDto(event);
    }

    public EventDto create(EventDto dto, MultipartFile file, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        Event event = EventMapper.INSTANCE.toEntity(dto);

        setClient(event,token);

        event = repository.save(event);

        log.info("The Event" + event.getName() + "was created sucessful");

        return EventMapper.INSTANCE.toDto(event);

    }

    private void setClient(Event event, String token) {
        event.setClient(tenantService.fetchClientByTenantFromToken(token));
    }

    public EventDto update(Long id, EventDto dto, MultipartFile file, String token) {
        String tenant = jwtUtils.getUserTenant(token);

        Event event = repository.findByIdAndClient_Tenant(id,tenant).orElseThrow(
                ()-> new EventNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Event", "id", id.toString())));

        BeanUtils.copyProperties(dto,event, "id");

        event = repository.save(event);

        log.info("The Event" + dto.getNameEvent()+"Was updated succesful");
        return EventMapper.INSTANCE.toDto(event);
    }

    public void delete(@NonNull Long id,@NonNull String token) {
        String tenant = jwtUtils.getUserTenant(token);

        Event event = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                ()-> new EventNotFoundExecption(ExceptionMessageFactory.createNotFoundMessage("Event", "ID", id.toString())));

        repository.delete(event);
    }


}

