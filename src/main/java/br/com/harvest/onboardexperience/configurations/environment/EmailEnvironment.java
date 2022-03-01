package br.com.harvest.onboardexperience.configurations.environment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailEnvironment {

    private String host;
    private String port;
    private String username;
    private String password;

    @Value("${spring.mail.properties.mail.smtp.port}")
    private String smtpPort;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private Boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private Boolean smtpStartTlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private Boolean smtpStartTlsRequired;

    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private Boolean smtpSslEnable;

}
