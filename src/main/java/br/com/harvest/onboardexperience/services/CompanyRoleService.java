package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.utils.GenericUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Service
public class CompanyRoleService {

    @Autowired
    private CompanyRoleRepository repository;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private TenantService tenantService;


    public CompanyRoleDto create(@NonNull CompanyRoleDto dto, @NonNull final String token) {
        dto.setClient(tenantService.fetchClientDtoByTenantFromToken(token));

        CompanyRole companyRole = repository.save(CompanyRoleMapper.INSTANCE.toEntity(dto));
        return CompanyRoleMapper.INSTANCE.toDto(companyRole);

    }


    public CompanyRoleDto update(@NonNull final Long id, @NonNull CompanyRoleDto dto, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);

        CompanyRole companyRole = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage(
                        "company role", "ID", id.toString())));

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

    public CompanyRoleDto findByIdOrNameAndTenant(String name, String tenant) {

        CompanyRole companyRole = repository.findByNameContainingIgnoreCaseAndClient_Tenant(name, tenant)
                .orElseThrow(() -> new CompanyRoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage(
                        "company role", "name", name)));

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

    public void disableAllByClient(@NonNull final Client client) {
        repository.disableAllByClient(client.getId());
    }
}
