package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.GroupDto;
import br.com.harvest.onboardexperience.domain.dtos.GroupForm;
import br.com.harvest.onboardexperience.domain.dtos.GroupSimpleDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.entities.Group;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.GroupNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.CompanyRoleMapper;
import br.com.harvest.onboardexperience.mappers.GroupMapper;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.GroupRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
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
        Client client = tenantService.fetchClientByTenantFromToken(token);

        Group group = Group.builder().client(client)
                .name(dto.getName())
                .companyRoles(fetchCompanyRoles(dto.getCompanyRoles(), client.getTenant()))
                .isActive(dto.getIsActive())
                .users(fetchUsers(dto.getUsers(), client.getTenant())).build();

        group = repository.save(group);
        return GroupMapper.INSTANCE.toDto(group);
    }

    public GroupDto update(@NonNull final Long id, @NonNull GroupForm dto, @NonNull final String token) {
        String tenant = jwtTokenUtils.getUserTenant(token);

        Group group = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new GroupNotFoundException(ExceptionMessageFactory.createNotFoundMessage("group",
                        "ID", id.toString())));

        Group updatedGroup = Group.builder()
                             .companyRoles(fetchCompanyRoles(dto.getCompanyRoles(), tenant))
                             .users(fetchUsers(dto.getUsers(), tenant))
                             .name(dto.getName())
                             .isActive(dto.getIsActive()).build();

        BeanUtils.copyProperties(updatedGroup, group, "id", "client", "createdBy", "createdAt");

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

    private List<User> fetchUsers(List<Long> usersId, String tenant){
        List<User> users = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(usersId)){
            for(Long userId : usersId){
                User user = UserMapper.INSTANCE.toEntity(userService.findByIdAndTenant(userId, tenant));
                users.add(user);
            }
        }
        return users;
    }

    private List<CompanyRole> fetchCompanyRoles(List<Long> companyRolesId, String tenant){
        List<CompanyRole> companyRoles = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(companyRolesId)){
            for(Long companyRoleId : companyRolesId){
                CompanyRole companyRole = CompanyRoleMapper.INSTANCE.toEntity(companyRoleService
                        .findByIdAndTenant(companyRoleId, tenant));
                companyRoles.add(companyRole);
            }
        }
        return companyRoles;
    }

}
