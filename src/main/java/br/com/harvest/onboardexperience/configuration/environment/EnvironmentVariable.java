package br.com.harvest.onboardexperience.configuration.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import br.com.harvest.onboardexperience.configuration.environment.enumerators.ProfileEnvironment;
import lombok.Getter;

@Getter
@Configuration
public class EnvironmentVariable {
	
	@Autowired
	private ApplicationEnvironment applicationEnvironment;
	 
	@Autowired
	private DataSourceEnvironment dataSourceEnvironment;
	
	@Value("${server.port}")
	private Integer serverPort;
	
	@Value("${spring.config.activate.on-profile}")
	private ProfileEnvironment profile;
	
	public String getApplicationVersion() {
		return applicationEnvironment.getVersion();
	}
	
	public String getApplicationDescription() {
		return applicationEnvironment.getDescription();
	}
	
	public String getApiVersion() {
		return applicationEnvironment.getApi().getVersion();
	}
	
	public String getDataSourceUsername() {
		return dataSourceEnvironment.getUsername();
	}
	
	public String getDataSourcePassword() {
		return dataSourceEnvironment.getPassword();
	}
	
	public String getDataSourceUrl() {
		return dataSourceEnvironment.getUrl();
	}

}
