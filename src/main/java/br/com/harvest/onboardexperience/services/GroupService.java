package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.*;
import br.com.harvest.onboardexperience.domain.dtos.forms.GroupForm;
import br.com.harvest.onboardexperience.domain.entities.Group;
import br.com.harvest.onboardexperience.domain.exceptions.GroupNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.mappers.GroupMapper;
import br.com.harvest.onboardexperience.repositories.GroupRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository repository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRoleService companyRoleService;

    public GroupDto create(@NonNull GroupForm dto, @NonNull final String token) {

        GroupDto groupDto = convertFormToGroupDto(dto, token);

        Group group = repository.save(GroupMapper.INSTANCE.toEntity(groupDto));
        return GroupMapper.INSTANCE.toDto(group);
    }

    public GroupDto update(@NonNull final Long id, @NonNull GroupForm dto, @NonNull final String token) {
        String tenant = jwtTokenUtils.getUserTenant(token);

        Group group = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new GroupNotFoundException(ExceptionMessageFactory.createNotFoundMessage("group",
                        "ID", id.toString())));


        GroupDto updatedGroup = convertFormToGroupDto(dto, token);

        GroupMapper.INSTANCE.updateEntity(updatedGroup, group);

        group = repository.save(group);

        return GroupMapper.INSTANCE.toDto(group);
    }

    public GroupDto findByIdAndTenant(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtTokenUtils.getUserTenant(token);

        Group group = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new GroupNotFoundException(ExceptionMessageFactory.createNotFoundMessage("group",
                        "ID", id.toString())));

        return GroupMapper.INSTANCE.toDto(group);
    }

    public Page<GroupSimpleDto> findAllByTenant(Pageable pageable, @NonNull final String token) {
        String tenant = jwtTokenUtils.getUserTenant(token);
        return repository.findAllByClient_Tenant(tenant, pageable).map(GroupMapper.INSTANCE::toGroupSimpleDto);
    }

    public void delete(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtTokenUtils.getUserTenant(token);
        Group group = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new GroupNotFoundException(ExceptionMessageFactory.createNotFoundMessage("group",
                        "ID", id.toString())));

        repository.delete(group);
    }

    private List<UserDto> fetchUsers(List<Long> usersId, String token){
        List<UserDto> users = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(usersId)){
            for(Long userId : usersId){
                UserDto user = userService.findByIdAndTenant(userId, token);
                users.add(user);
            }
        }
        return users;
    }

    private List<CompanyRoleDto> fetchCompanyRoles(List<Long> companyRolesId, String token){
        List<CompanyRoleDto> companyRoles = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(companyRolesId)){
            for(Long companyRoleId : companyRolesId){
                CompanyRoleDto companyRole = companyRoleService.findByIdAndTenant(companyRoleId, token);
                companyRoles.add(companyRole);
            }
        }
        return companyRoles;
    }

    private GroupDto convertFormToGroupDto(@NonNull GroupForm form, String token){
        ClientDto client = ClientMapper.INSTANCE.toDto(tenantService.fetchClientByTenantFromToken(token));

        return GroupDto.builder().client(client)
                .name(form.getName())
                .companyRoles(fetchCompanyRoles(form.getCompanyRoles(), token))
                .isActive(form.getIsActive())
                .users(fetchUsers(form.getUsers(), token))
                .build();
    }

}
