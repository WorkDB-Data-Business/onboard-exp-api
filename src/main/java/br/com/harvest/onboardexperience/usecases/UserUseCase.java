package br.com.harvest.onboardexperience.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.management.relation.RoleNotFoundException;
import javax.servlet.http.HttpServletRequest;

import br.com.harvest.onboardexperience.domain.dtos.forms.ChangePasswordForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.EmailForm;
import br.com.harvest.onboardexperience.domain.dtos.forms.UserWelcomeForm;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.exceptions.PasswordResetTokenExpiredException;
import br.com.harvest.onboardexperience.domain.exceptions.PasswordResetTokenNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.UserNotFoundException;
import br.com.harvest.onboardexperience.infra.email.dtos.EmailMessage;
import br.com.harvest.onboardexperience.infra.email.interfaces.EmailSender;
import br.com.harvest.onboardexperience.repositories.PasswordResetTokenRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.harvest.onboardexperience.configurations.application.PasswordConfiguration;
import br.com.harvest.onboardexperience.domain.enumerators.RoleEnum;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.repositories.CompanyRoleRepository;
import br.com.harvest.onboardexperience.repositories.RoleRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@Component
public class UserUseCase {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRoleRepository companyRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordConfiguration passwordConfiguration;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailSender emailSender;

    public Boolean createAdminUserFromClient(@NonNull final Client client) {
        try {

            Role role = roleRepository.findByRole(RoleEnum.ADMIN).orElseThrow(
                    () -> new RoleNotFoundException(ExceptionMessageFactory.createNotFoundMessage("role",
                            "name", RoleEnum.ADMIN.getName())));
            CompanyRole companyRole = createAdminCompanyRoleFromClient(client);
            User user = User.builder().client(client)
                    .companyRole(companyRole)
                    .email(client.getEmail())
                    .isClient(true)
                    .username(GenericUtils.formatNameToUsername(client.getName()))
                    .firstName(client.getName())
                    .isActive(true)
                    .isFirstLogin(true)
                    .isBlocked(false)
                    .isChangePasswordRequired(true)
                    .isExpired(false)
                    .lastName("User")
                    .isFirstLogin(true)
                    .password(passwordConfiguration.encoder().encode(client.getCnpj()))
                    .roles(Set.of(role))
                    .build();

            if (userRepository.findByEmailContainingIgnoreCase(user.getEmail()).isPresent()) {
                return false;
            }
            userRepository.save(user);
            log.info("Admin user from client of ID " + client.getId() + " created successful.");
            return true;
        } catch (Exception e) {
            log.error("An error has occurred while creating the admin user from client of ID " + client.getId());
            return false;
        }
    }

    private CompanyRole createAdminCompanyRoleFromClient(@NonNull final Client client) {
        try {
            CompanyRole companyRole = companyRoleRepository.save(CompanyRole.builder()
                    .isActive(true)
                    .client(client)
                    .name(client.getName() + " Admin User")
                    .build());
            log.info("Admin company role from client of ID " + client.getId() + " created successful.");
            return companyRole;
        } catch (Exception e) {
            log.error("An error has occurred while creating company role admin from client of ID " + client.getId(), e);
            return null;
        }
    }

    public void welcomeUser(@NonNull final Long id, @NonNull UserWelcomeForm form, final String token){
        String tenant = jwtTokenUtils.getUserTenant(token);

        User user = userRepository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));

        user.setIdAvatar(form.getIdAvatar());
        user.setNickname(form.getNickname());

        userRepository.save(user);
    }

    public void changePassword(@NonNull final Long id, @NonNull ChangePasswordForm form, final String token){
        String tenant = jwtTokenUtils.getUserTenant(token);

        User user = userRepository.findByIdAndClient_Tenant(id, tenant).orElseThrow(
                () -> new UserNotFoundException(ExceptionMessageFactory.createNotFoundMessage("user", "ID", id.toString())));

        user.setPassword(passwordConfiguration.encoder().encode(form.getPassword()));
        user.setIsChangePasswordRequired(false);

        userRepository.save(user);
    }

    public void sendEmailToResetPassword(@NonNull EmailForm form, HttpServletRequest request) throws Exception {
        User user = userRepository.findByEmailContainingIgnoreCase(form.getEmail()).orElse(null);

        if(user == null){
            return;
        }

        String token = UUID.randomUUID().toString();

        createPasswordResetToken(user, token);

        sendResetPasswordEmail(user, token, request);
    }

    public void resetPassword(@NonNull String token, ChangePasswordForm passwordForm){
        validatePasswordResetToken(token);

        User user = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new PasswordResetTokenNotFoundException("The password reset token is invalid or was not found")
        ).getUser();

        user.setPassword(passwordConfiguration.encoder().encode(passwordForm.getPassword()));

        userRepository.save(user);
    }

    public void expireAllPasswordResetTokenByUser(@NonNull User user){
        List<PasswordResetToken> passwordResetTokens = passwordResetTokenRepository.findAllByUserAndIsExpired(user, false);

        if(ObjectUtils.isNotEmpty(passwordResetTokens)) {
            passwordResetTokens.stream().forEach(passwordResetToken -> expirePasswordResetToken(passwordResetToken));
        }
    }

    public void validatePasswordResetToken(@NonNull String token){
        final PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new PasswordResetTokenNotFoundException("The password reset token is invalid or was not found")
        );

        if(isTokenExpired(passwordResetToken)) {
            expirePasswordResetToken(passwordResetToken);
            throw new PasswordResetTokenExpiredException("The password reset token is already expired");
        }
        expirePasswordResetToken(passwordResetToken);
    }

    private void expirePasswordResetToken(PasswordResetToken passwordResetToken){
        passwordResetToken.setIsExpired(true);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    private Boolean isTokenExpired(PasswordResetToken passwordResetToken) {
        return passwordResetToken.getExpirationTime().isBefore(LocalDateTime.now()) || passwordResetToken.getIsExpired();
    }

    private EmailMessage createResetPasswordEmail(User user, String token, HttpServletRequest request){
        return EmailMessage.builder()
                .model("forgot_password.html")
                .receivers(Set.of(user.getEmail()))
                .subject("Recuperação de senha")
                .variables(Map.of("name", user.getNickname(), "link", buildLinkForPasswordReset(token, request)))
                .build();
    }

    private void sendResetPasswordEmail(User user, String token, HttpServletRequest request) throws Exception {
        emailSender.send(createResetPasswordEmail(user, token, request));
    }

    private String buildLinkForPasswordReset(String token, HttpServletRequest request){
        String serverURL = request.getHeader("origin");
        StringBuilder builder = new StringBuilder(serverURL)
                .append("/")
                .append("signin")
                .append("?")
                .append("token=")
                .append(token);
        return builder.toString();
    }

    private void createPasswordResetToken(User user, String token){
        expireAllPasswordResetTokenByUser(user);

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expirationTime(LocalDateTime.now().plusHours(1))
                .isExpired(false)
                .build();
        passwordResetTokenRepository.save(passwordResetToken);
    }

}
