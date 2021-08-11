package br.com.harvest.onboardexperience.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.harvest.onboardexperience.domain.dto.RoleDto;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.exceptions.RoleNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.RoleMapper;
import br.com.harvest.onboardexperience.repositories.RoleRepository;
import lombok.NonNull;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleMapper mapper;

    public RoleDto findRoleById(@NonNull Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() ->
                new RoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("role", "ID", id.toString()))));
    }

    public RoleDto findRoleByRole(RoleEnum roleEnum) {
        return mapper.toDto(repository.findByRole(roleEnum).orElseThrow(() ->
                new RoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("role", "name", roleEnum.getName()))));
    }

    @Transactional
    public void deleteRelationshipFromUser(@NonNull Long idUser) {
        if (repository.getCountOfRolesFromUser(idUser) > 0) {
            repository.deleteRelationshipFromUser(idUser);
        }
    }

}
