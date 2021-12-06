package br.com.harvest.onboardexperience.services;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.CompanyRole;
import br.com.harvest.onboardexperience.domain.entities.Role;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.mappers.RoleMapper;
import br.com.harvest.onboardexperience.utils.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.harvest.onboardexperience.configurations.application.PasswordConfiguration;
import br.com.harvest.onboardexperience.domain.dtos.RoleDto;
import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.UserForm;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.InvalidCpfException;
import br.com.harvest.onboardexperience.domain.exceptions.UserAlreadyExistsException;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordConfiguration passwordConfiguration;

    @Autowired
    private CompanyRoleService companyRoleService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private TenantService tenantService;

    @Value(Constants.Harvest.User.USERNAME)
    private String harvestUserUsername;

    @Value(Constants.Harvest.User.PASSWORD)
    private String harvestUserPassword;

    @Value(Constants.Harvest.User.FIRST_NAME)
    private String harvestUserFirstName;

    @Value(Constants.Harvest.User.LAST_NAME)
    private String harvestUserLastName;

    @Value(Constants.Harvest.User.EMAIL)
    private String harvestUserEmail;


    public UserDto create(@NonNull UserForm dto, String token) {
        UserDto userDto = convetFormToUserDto(dto, token);

        validateUser(userDto);

        User user = repository.save(UserMapper.INSTANCE.toEntity(userDto));

        log.info("The user " + userDto.getUsername() + " was saved successful.");
        return UserMapper.INSTANCE.toDto(user);

    }

    private UserDto convetFormToUserDto(UserForm form, String token) {
        var userDto = new UserDto();

        userDto.setFirstName(form.getFirstName());
        userDto.setLastName(form.getLastName());
        userDto.setUsername(form.getUsername());
        userDto.setPassword(form.getCpf());
        userDto.setEmail(form.getEmail());
        userDto.setCpf(form.getCpf());
        userDto.setIsActive(form.getIsActive());
        userDto.setIsBlocked(form.getIsBlocked());
        userDto.setClient(tenantService.fetchClientDtoByTenantFromToken(token));
        userDto.setCompanyRole(companyRoleService.findByIdAndTenant(form.getCompanyRoleId(), token));
        userDto.setRoles(convertUserRoles(form));


        return userDto;
    }

    private Set<RoleDto> convertUserRoles(@NonNull UserForm form){
        Set<RoleDto> rolesDto = new HashSet<>();

        if (form.getIsAdmin()) rolesDto.add(RoleMapper.INSTANCE.toDto(roleService.findRoleByRoleEnum(RoleEnum.ADMIN)));
        if (form.getIsCol()) rolesDto.add(RoleMapper.INSTANCE.toDto(roleService.findRoleByRoleEnum(RoleEnum.EMPLOYEE)));

        return rolesDto;
    }


    public UserDto update(@NonNull Long id, @NonNull UserForm dto, String token) {
        UserDto userDto = convetFormToUserDto(dto, token);

        User user = repository.findByIdAndClient_Tenant(id, userDto.getClient().getTenant()).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));

        // TODO: create method to update password only.
        validateUser(user, userDto);

        BeanUtils.copyProperties(UserMapper.INSTANCE.toEntity(userDto), user,  "id", "client", "createdBy", "createdAt", "password");

        user = repository.save(user);

        log.info("The user " + userDto.getUsername() + " was updated successful.");

        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional
    public void disableUser(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);

        User user = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
        user.setIsActive(!user.getIsActive());
        repository.save(user);

        String isEnabled = user.getIsActive().equals(true) ? "disabled" : "enabled";
        log.info("The user with ID " + id + " was " + isEnabled + " successful.");

    }

    @Transactional
    public void expireUser(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        User user = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
        user.setIsExpired(!user.getIsExpired());
        repository.save(user);

        String isExpired = user.getIsExpired().equals(true) ? "expired" : "expired reverted";
        log.info("The user with ID " + id + " was " + isExpired + " successful.");
    }

    @Transactional
    public void blockUser(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);

        User user = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
        user.setIsBlocked(!user.getIsBlocked());
        repository.save(user);
        String isBlocked = user.getIsBlocked().equals(true) ? "blocked" : "desblocked";
        log.info("The user with ID " + id + " was " + isBlocked + " successful.");
    }


    public UserDto findUserDtoByIdAndTenant(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        User user = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));

        return UserMapper.INSTANCE.toDto(user);
    }

    public User findUserByIdAndTenant(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        return repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
    }

    public UserDto findMyUser(@NonNull final String token) {
        Long idUser = jwtUtils.getUserId(token);
        User user = repository.findById(idUser).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", idUser.toString())));

        return UserMapper.INSTANCE.toDto(user);
    }

    public Page<UserDto> findByCriteria(String criteria,final Pageable pageable, final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        if(GenericUtils.stringNullOrEmpty(criteria)){
            return findAllByTenant(pageable, token);
        }
        return repository.findByCriteria(criteria, tenant, pageable).map(UserMapper.INSTANCE::toDto);
    }

    public Page<UserDto> findAllByTenant(final Pageable pageable, final String token) {
        String tenant = jwtUtils.getUserTenant(token);
        return repository.findAllByClient_Tenant(tenant, pageable).map(UserMapper.INSTANCE::toDto);
    }


    public void delete(@NonNull final Long id, @NonNull final String token) {
        String tenant = jwtUtils.getUserTenant(token);

        User user = repository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
        roleService.deleteRelationshipFromUser(id);
        repository.delete(user);
        log.info("The user with ID " + id + " was deleted successful.");

    }

    public User findUserByEmail(@NonNull String email) {
        return repository.findByEmailContainingIgnoreCase(email).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "email", email)));
    }

    public User findUserById(@NonNull Long id) {
        return repository.findById(id).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));
    }

    public User findUserByToken(@NonNull String token){
        return  findUserById(jwtUtils.getUserId(token));
    }

    private void encryptPassword(@NonNull UserDto user) {
        user.setPassword(passwordConfiguration.encoder().encode(user.getPassword()));
    }

    private void checkIfUserAlreadyExists(@NonNull UserDto dto) {
        if (repository.findByUsernameContainingIgnoreCaseAndClient(dto.getUsername(), ClientMapper.INSTANCE.toEntity(dto.getClient())).isPresent()) {
            throw new UserAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("user", "username", dto.getUsername()));
        }

        if (repository.findByEmailContainingIgnoreCase(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("user", "email", dto.getEmail()));
        }

        if (ObjectUtils.isNotEmpty(dto.getCpf()) && repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new UserAlreadyExistsException(ExceptionMessageFactory.createAlreadyExistsMessage("user", "CPF", dto.getCpf()));
        }
    }

    private void checkIfUserAlreadyExists(@NonNull User user, @NonNull UserDto dto) {
        if (!checkIfEmailChanged(user, dto)) {
            return;
        } else if (!checkIfUsernameChanged(user, dto)) {
            return;
        } else if (!checkIfCpfChanged(user, dto)) {
            return;
        }
        checkIfUserAlreadyExists(dto);
    }

    private Boolean checkIfUsernameChanged(@NonNull final User user, @NonNull final UserDto dto) {
        if (user.getUsername().equals(dto.getUsername())) {
            return false;
        }

        return true;
    }

    private Boolean checkIfEmailChanged(@NonNull final User user, @NonNull final UserDto dto) {
        if (user.getEmail().equalsIgnoreCase(dto.getEmail())) {
            return false;
        }
        return true;
    }

    private void validateCpf(@NonNull final UserDto dto) {

        if (ObjectUtils.isNotEmpty(dto.getCpf()) && !GenericUtils.validateCPF(dto.getCpf())) {
            throw new InvalidCpfException(dto.getCpf());
        }

    }

    private void validateCpf(@NonNull final User user, @NonNull final UserDto dto) {
        if (checkIfCpfChanged(user, dto)) {
            validateCpf(dto);
        }
    }

    private Boolean checkIfCpfChanged(@NonNull User user, @NonNull UserDto dto) {
        Boolean hasCpf = Objects.nonNull(user.getCpf()) && Objects.nonNull(dto.getCpf());
        if (hasCpf && user.getCpf().equals(dto.getCpf())) {
            return false;
        }
        return true;
    }

    private void validateUser(@NonNull UserDto dto) {

        checkIfUserAlreadyExists(dto);

        validateCpf(dto);

        encryptPassword(dto);

    }

    private void validateUser(@NonNull User user, @NonNull UserDto dto) {

        checkIfUserAlreadyExists(user, dto);

        validateCpf(user, dto);

    }

    private User createHarvestUser(Client client, CompanyRole companyRole, Role masterRole){
        return User.builder()
                .id(Constants.Harvest.User.ID)
                .firstName(harvestUserFirstName)
                .lastName(harvestUserLastName)
                .email(harvestUserEmail)
                .username(harvestUserUsername)
                .password(passwordConfiguration.encoder().encode(harvestUserPassword))
                .isChangePasswordRequired(true)
                .isActive(true)
                .isFirstLogin(true)
                .isExpired(false)
                .isBlocked(false)
                .client(client)
                .isClient(true)
                .companyRole(companyRole)
                .roles(Set.of(masterRole))
                .build();
    }

    private Boolean needToImport(User user){
        return repository.findById(user.getId()).isEmpty();
    }

    public void saveHarvestUser() {

        try {
            Optional.of(createHarvestUser(clientService.getHarvestClient(),
                            companyRoleService.getHarvestCompanyRole(),
                            roleService.findRoleByRoleEnum(RoleEnum.MASTER)))
                    .filter(this::needToImport)
                    .ifPresent(user -> {
                        repository.save(user);
                        log.info("The load of harvest user occurred successful");
                    });

        } catch (Exception e) {
            log.error("Occurred an error to load harvest user: " + e.getMessage(), e.getCause());
        }
    }

}
