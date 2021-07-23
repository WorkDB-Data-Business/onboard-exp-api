package br.com.harvest.onboardexperience.configurations.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
class DataSourceEnvironment {

	private String url;
	private String username;
	private String password;
	
}
