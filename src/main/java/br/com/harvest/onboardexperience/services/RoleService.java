package br.com.harvest.onboardexperience.services;


import br.com.harvest.onboardexperience.domain.entities.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.harvest.onboardexperience.domain.dtos.RoleDto;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.exceptions.RoleNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.RoleMapper;
import br.com.harvest.onboardexperience.repositories.RoleRepository;
import lombok.NonNull;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleMapper mapper;

    @Autowired
    private PermissionService permissionService;

    public RoleDto findRoleById(@NonNull Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() ->
                new RoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("role", "ID", id.toString()))));
    }

    public Role findRoleByRoleEnum(RoleEnum roleEnum) {
        return repository.findByRole(roleEnum).orElseThrow(() ->
                new RoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("role", "name", roleEnum.getName())));
    }

    @Transactional
    public void deleteRelationshipFromUser(@NonNull Long idUser) {
        if (repository.getCountOfRolesFromUser(idUser) > 0) {
            repository.deleteRelationshipFromUser(idUser);
        }
    }

    public Role createRoleByEnum(RoleEnum roleEnum){
        return Role.builder().role(roleEnum).permissions(permissionService.findPermissionsByRoleEnum(roleEnum)).build();
    }

    public void populateRoles(){
        RoleEnum.getRolesAsList().stream()
                .filter(this::needToImport)
                .map(this::createRoleByEnum)
                .forEach(this::saveRole);
    }

    private void saveRole(Role role){
        try {
            repository.save(role);
            log.info(MessageFormat.format("The load of {0} role occurred successful", role.getRole()));
        } catch (Exception e){
            log.error(MessageFormat.format("Occurred an error to load role: {0}", role.getRole()), e.getCause());
        }
    }

    private Boolean needToImport(RoleEnum roleEnum){
        return repository.findByRole(roleEnum).isEmpty();
    }

}
