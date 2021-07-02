package br.com.harvest.onboardexperience.configuration.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.api")
class ApiEnvironment {
	
	private String version;

}
