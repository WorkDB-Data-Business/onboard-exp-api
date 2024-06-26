package br.com.harvest.onboardexperience.configurations.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.harvest.onboardexperience.configurations.environment.EnvironmentVariable;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class OpenApiConfig {
	
	  @Autowired
	  private EnvironmentVariable env;
	  
	  @Bean
	  public OpenAPI springShopOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Onboard Experience API")
	              .description(env.getApplicationEnvironment().getDescription())
	              .version(env.getApiEnvironment().getVersion()));
	      		
	  }

}
