package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.entities.Permission;
import br.com.harvest.onboardexperience.domain.enumerators.PermissionEnum;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.exceptions.PermissionNotFoundException;
import br.com.harvest.onboardexperience.repositories.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository repository;

    public Permission createPermission(PermissionEnum permission){
        return Permission.builder().permission(permission).build();
    }

    public Permission findPermissionByEnum(PermissionEnum permissionEnum){
       return repository.findByPermission(permissionEnum)
                .orElseThrow(() -> new PermissionNotFoundException(
                        MessageFormat.format("Permission with name {0} was not found", permissionEnum.getName())));
    }

    public void populatePermissions(){
        PermissionEnum.getPermissionsAsList().stream()
                .filter(this::needToImport)
                .map(this::createPermission)
                .forEach(this::savePermission);
    }

    private Boolean needToImport(PermissionEnum permissionEnum){
        return repository.findByPermission(permissionEnum).isEmpty();
    }

    private void savePermission(Permission permission){
        try {
            repository.save(permission);
            log.info(MessageFormat.format("The load of {0} permission  occurred successful", permission.getPermission()));
        } catch (Exception e){
            log.error("Occurred an error to load permissions: " + e.getMessage(), e.getCause());
        }
    }

    public Set<Permission> findPermissionsByRoleEnum(RoleEnum roleEnum){
        return roleEnum.getPermissions().stream().map(this::findPermissionByEnum).collect(Collectors.toSet());
    }

}
