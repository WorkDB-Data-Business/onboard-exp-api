package br.com.harvest.onboardexperience.configurations.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import br.com.harvest.onboardexperience.configurations.environment.enumerators.ProfileEnvironment;
import lombok.Getter;

@Getter
@Configuration
public class EnvironmentVariable {
	
	@Autowired
	private ApplicationEnvironment applicationEnvironment;
	 
	@Autowired
	private DataSourceEnvironment dataSourceEnvironment;

	@Autowired
	private ApiEnvironment apiEnvironment;

	@Autowired
	private EmailEnvironment emailEnvironment;
	
	@Value("${server.port}")
	private Integer serverPort;
	
	@Value("${spring.config.activate.on-profile}")
	private ProfileEnvironment profile;
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	public ApiEnvironment getApiEnvironment() {
		return this.apiEnvironment;
	}
	
	public ApplicationEnvironment getApplicationEnvironment() {
		return this.applicationEnvironment;
	}

	public DataSourceEnvironment getDataSourceEnvironment(){
		return this.dataSourceEnvironment;
	}

	public EmailEnvironment getEmailEnvironment(){
		return this.emailEnvironment;
	}

}
