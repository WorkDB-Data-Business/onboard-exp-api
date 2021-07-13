package br.com.harvest.onboardexperience.configurations.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
class ApplicationEnvironment {
	
	private String version;
	private String description;
	private ApiEnvironment api;

}
