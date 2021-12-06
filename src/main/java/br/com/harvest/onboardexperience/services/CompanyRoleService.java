package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.CompanyRoleForm;
import br.com.harvest.onboardexperience.domain.exceptions.CompanyRoleAlreadyExistsException;
import br.com.harvest.onboardexperience.utils.Constants;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.dtos.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.exceptions.CompanyRoleNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.CompanyRoleMapper;
import br.com.harvest.onboardexperience.repositories.CompanyRoleRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Service
public class CompanyRoleService {

    @Autowired
    private CompanyRoleRepository repository;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private TenantService tenantService;

    @Value(Constants.Harvest.CompanyRole.NAME)
    private String harvestCompanyRoleName;

    @Autowired
    private ClientService clientService;

    public CompanyRoleDto create(@NonNull CompanyRoleForm form, @NonNull final String token) {
        CompanyRoleDto dto = convertFormToCompanyRoleDto(form, token);

        validate(dto, jwtUtils.getUserTenant(token));

        CompanyRole companyRole = repository.save(CompanyRoleMapper.INSTANCE.toEntity(dto));
        return CompanyRoleMapper.INSTANCE.toDto(companyRole);
    }


    public CompanyRoleDto update(@NonNull final Long id, @NonNull CompanyRoleForm form, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);

        CompanyRole companyRole = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage(
                        "company role", "ID", id.toString())));

        CompanyRoleDto dto = convertFormToCompanyRoleDto(form, token);

        validate(companyRole, dto, tenant);

        BeanUtils.copyProperties(dto, companyRole, "id", "client", "createdBy", "createdAt");

        companyRole = repository.save(companyRole);

        return CompanyRoleMapper.INSTANCE.toDto(companyRole);
    }

    public Page<CompanyRoleDto> findByCriteria(String criteria, Pageable pageable, String token) {
        String tenant = jwtUtils.getUserTenant(token);
        if(GenericUtils.stringNullOrEmpty(criteria)){
            return findAllByTenant(pageable, token);
        }
        return repository.findByCriteria(criteria, tenant, pageable).map(CompanyRoleMapper.INSTANCE::toDto);
    }


    public CompanyRoleDto findByIdAndTenant(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);

        CompanyRole companyRole = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage(
                        "company role", "ID", id.toString())));

        return CompanyRoleMapper.INSTANCE.toDto(companyRole);
    }

    public CompanyRoleDto findByIdOrNameAndTenant(String name, Long id, String tenant) {

        CompanyRole companyRole = repository.findByNameContainingIgnoreCaseOrIdAndClient_Tenant(name, id, tenant)
                .orElseThrow(() -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage(
                        "company role", ObjectUtils.isEmpty(name) ? "ID" : "name", ObjectUtils.isEmpty(name) ? id.toString() : name)));

        return CompanyRoleMapper.INSTANCE.toDto(companyRole);
    }

    public Page<CompanyRoleDto> findAllByTenant(Pageable pageable, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        return repository.findAllByClient_Tenant(tenant, pageable).map(CompanyRoleMapper.INSTANCE::toDto);
    }

    public void delete(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        CompanyRole companyRole = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage(
                        "company role", "ID", id.toString())));
        repository.delete(companyRole);
    }

    public void validate(@NonNull CompanyRoleDto dto, String tenant){
        if(repository.findByNameContainingIgnoreCaseAndClient_Tenant(dto.getName(), tenant).isPresent()){
            throw new CompanyRoleAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("company role",
            "name", dto.getName()));
        }
    }

    public void validate(@NonNull CompanyRole companyRole, CompanyRoleDto dto, String tenant){
        if(companyRole.getName().equalsIgnoreCase(dto.getName())){
            validate(dto, tenant);
        }
    }

    private CompanyRoleDto convertFormToCompanyRoleDto(@NonNull CompanyRoleForm form, String token){
        ClientDto client = tenantService.fetchClientDtoByTenantFromToken(token);
        return CompanyRoleDto.builder()
                .client(client)
                .isActive(form.getIsActive())
                .name(form.getName()).build();
    }

    public void disableAllByClient(@NonNull final Client client) {
        repository.disableAllByClient(client.getId());
    }

    public void saveHarvestCompanyRole() {
        try {
            Optional.of(createHarvestCompanyRole()).filter(this::needToImport)
                    .ifPresent(companyRole -> {
                        repository.save(companyRole);
                        log.info("The load of harvest user's company role occurred successful");
                    });

        } catch (Exception e) {
            log.error(MessageFormat.format("Occurred an error to load harvest user's company role: {1}", e.getMessage()), e.getCause());
        }
    }

    public CompanyRole getHarvestCompanyRole(){
        return repository.getById(Constants.Harvest.CompanyRole.ID);
    }

    private Boolean needToImport(CompanyRole companyRole){
        return repository.findByNameContainingIgnoreCase(companyRole.getName()).isEmpty();
    }

    private CompanyRole createHarvestCompanyRole(){
        return CompanyRole.builder()
                .isActive(true)
                .name(harvestCompanyRoleName)
                .client(clientService.getHarvestClient()).build();
    }

}
