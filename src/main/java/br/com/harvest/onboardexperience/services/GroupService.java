package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.*;
import br.com.harvest.onboardexperience.domain.dtos.forms.GroupForm;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.entities.Group;
import br.com.harvest.onboardexperience.domain.entities.User;
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
import java.util.stream.Collectors;

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

    @Autowired
    private FetchService fetchService;

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

    public GroupForm findByIdAndTenant(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtTokenUtils.getUserTenant(token);

        Group group = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new GroupNotFoundException(ExceptionMessageFactory.createNotFoundMessage("group",
                        "ID", id.toString())));

        return convertGroupToFormDto(group);
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



    private GroupDto convertFormToGroupDto(@NonNull GroupForm form, String token){
        ClientDto client = ClientMapper.INSTANCE.toDto(tenantService.fetchClientByTenantFromToken(token));

        return GroupDto.builder().client(client)
                .name(form.getName())
                .companyRoles(fetchService.fetchCompanyRoles(form.getCompanyRoles(), token))
                .isActive(form.getIsActive())
                .users(fetchService.fetchUsers(form.getUsers(), token))
                .build();
    }

    private GroupForm convertGroupToFormDto(@NonNull Group group){
        GroupForm form = new GroupForm();
        form.setIsActive(group.getIsActive());
        form.setName(group.getName());
        form.setId(group.getId());
        form.setUsers(convertUsersToLong(group.getUsers()));
        form.setCompanyRoles(convertCompanyRolesToLong(group.getCompanyRoles()));
        return form;
    }

    private List<Long> convertUsersToLong(List<User> users){
        List<Long> ids = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(users)){
            ids = users.stream().mapToLong(user -> user.getId()).boxed().collect(Collectors.toList());
        }
        return ids;
    }

    private List<Long> convertCompanyRolesToLong(List<CompanyRole> companyRoles){
        List<Long> ids = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(companyRoles)){
            ids = companyRoles.stream().mapToLong(companyRole -> companyRole.getId()).boxed().collect(Collectors.toList());
        }
        return ids;
    }

}
