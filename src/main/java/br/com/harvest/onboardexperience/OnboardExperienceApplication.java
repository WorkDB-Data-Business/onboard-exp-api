package br.com.harvest.onboardexperience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableConfigurationProperties
public class OnboardExperienceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnboardExperienceApplication.class, args);
	}

}
