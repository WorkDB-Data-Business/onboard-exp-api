package br.com.harvest.onboardexperience.configurations.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.api")
public class ApiEnvironment {
	
	private String version;

}
