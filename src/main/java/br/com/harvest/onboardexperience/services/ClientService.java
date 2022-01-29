package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.exceptions.UserAlreadyExistsException;
import br.com.harvest.onboardexperience.utils.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.exceptions.ClientAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.ClientNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.InvalidCnpjException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.repositories.ClientRepository;
import br.com.harvest.onboardexperience.usecases.UserUseCase;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private ClientMapper mapper;

    @Autowired
    private UserUseCase generateUser;

    @Autowired
    private CompanyRoleService companyRoleService;

    @Value(Constants.Harvest.Client.CNPJ)
    private String harvestClientCnpj;

    @Value(Constants.Harvest.Client.EMAIL)
    private String harvestClientEmail;

    @Value(Constants.Harvest.Client.NAME)
    private String harvestClientName;

    @Value(Constants.Harvest.Client.TENANT)
    private String harvestClientTenant;


    @Transactional
    public ClientDto create(@NonNull ClientDto dto) throws Exception {
        validate(dto);

        Client client = repository.save(mapper.toEntity(dto));
        log.info("The client with CNPJ " + dto.getCnpj() + " was saved successful.");

        if(!generateUser.createAdminUserFromClient(client)){
            throw new UserAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("user", "email", client.getEmail()));
        }

        return mapper.toDto(client);
    }


    @Transactional
    public ClientDto update(@NonNull final Long id, @NonNull ClientDto dto) {
        Client client = repository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));

        validate(client, dto);

        BeanUtils.copyProperties(dto, client, "id", "createdAt", "createdBy");

        client = repository.save(client);

        log.info("The client with CNPJ " + dto.getCnpj() + " was updated successful.");

        return mapper.toDto(client);
    }


    public ClientDto findById(@NonNull final Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString()))));
    }

    public Page<ClientDto> findByCriteria(String criteria, Pageable pageable, String token) {
        if(GenericUtils.stringNullOrEmpty(criteria)){
            return findAll(pageable);
        }
        return repository.findByCriteria(criteria, pageable).map(mapper::toDto);
    }

    public ClientDto findByTenant(@NonNull final String tenant) {
        return mapper.toDto(repository.findByTenantContainingIgnoreCase(tenant).orElseThrow(
                () -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", tenant))));
    }

    public Page<ClientDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public List<ClientDto> findAll() {
        return repository.findAll().stream().filter(
                client -> !client.getId().equals(Constants.Harvest.Client.ID)
        ).map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void delete(@NonNull final Long id) {
        Client client = repository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));

        repository.delete(client);
        companyRoleService.disableAllByClient(client);
        repository.disableAllUsersFromAClient(false, client.getId());

    }

    @Transactional
    public void disableClient(@NonNull final Long id) {
        Client client = repository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));
        Boolean isActive = !client.getIsActive();
        client.setIsActive(isActive);
        repository.save(client);
        repository.disableAllUsersFromAClient(isActive, client.getId());

        String isEnabled = isActive ? "enabled" : "disabled";

        log.info("The client with ID " + id + " was " + isEnabled + " successful.");
        log.info("The users from the client with ID " + id + " was " + isEnabled + " successful.");
    }

    @Transactional
    public void expireClient(@NonNull final Long id) {
        Client client = repository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));
        Boolean isExpired = !client.getIsExpired();
        client.setIsExpired(isExpired);
        repository.save(client);
        repository.expireAllUsersFromAClient(isExpired, client.getId());

        String expired = isExpired ? "expired" : "expired reverted";

        log.info("The client with ID " + id + " was " + expired + " successful.");
        log.info("The users from the client with ID " + id + " was " + expired + " successful.");

    }

    @Transactional
    public void blockClient(@NonNull final Long id) {

        Client client = repository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(ExceptionMessageFactory.createNotFoundMessage("client", "ID", id.toString())));
        Boolean isBlocked = !client.getIsBlocked();
        client.setIsExpired(isBlocked);
        repository.save(client);
        repository.blockAllUsersFromAClient(isBlocked, client.getId());

        String blocked = isBlocked ? "blocked" : "disblocked";

        log.info("The client with ID " + id + " was " + blocked + " successful.");
        log.info("The users from the client with ID " + id + " was " + blocked + " successful.");
    }

    private void validateCnpj(@NonNull final ClientDto dto) {

        if (ObjectUtils.isNotEmpty(dto.getCnpj()) && !GenericUtils.validateCNPJ(dto.getCnpj())) {
            throw new InvalidCnpjException(dto.getCnpj());
        }

    }

    private void validateCnpj(@NonNull final Client client, @NonNull final ClientDto dto) {
        if (checkIfCnpjChanged(client, dto)) {
            validateCnpj(dto);
        }
    }

    private Boolean checkIfCnpjChanged(@NonNull final Client client, @NonNull final ClientDto dto) {
        if (client.getCnpj().equals(dto.getCnpj())) {
            return false;
        }
        return true;
    }

    private void validate(@NonNull ClientDto dto) {
        checkIfClientAlreadyExists(dto);
        validateCnpj(dto);
    }

    private void validate(@NonNull Client client, @NonNull ClientDto dto) {
        validateCnpj(client, dto);
    }

    private void checkIfClientAlreadyExists(@NonNull ClientDto dto) {
        if (repository.findByCnpj(dto.getCnpj()).isPresent()) {
            throw new ClientAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("client", "CNPJ", dto.getCnpj()));
        }
    }

    public Client getHarvestClient(){
        return ClientMapper.INSTANCE.toEntity(findById(Constants.Harvest.Client.ID));
    }

    private Boolean needToImport(Client client){
        return repository.findByNameContainingIgnoreCase(harvestClientName).isEmpty();
    }

    public void saveHarvestClient() {
        try {
            Optional.of(createHarvestClient())
                    .filter(this::needToImport)
                            .ifPresent(client -> {
                                repository.save(client);
                                log.info("The load of harvest client occurred successful");});
        } catch(Exception e) {
            log.error("Occurred an error to load harvest client: " + e.getMessage(), e.getCause());
        }
    }

    private Client createHarvestClient(){
       return Client.builder().cnpj(harvestClientCnpj)
                .id(Constants.Harvest.Client.ID)
                .name(harvestClientName)
                .isActive(true)
                .isBlocked(false)
                .isExpired(false)
                .isMaster(true)
                .tenant(harvestClientTenant)
                .email(harvestClientEmail)
                .build();
    }
}
