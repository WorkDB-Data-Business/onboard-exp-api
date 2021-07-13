package br.com.harvest.onboardexperience.configurations.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.api")
class ApiEnvironment {
	
	private String version;

}
