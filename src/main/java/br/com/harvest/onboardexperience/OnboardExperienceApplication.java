package br.com.harvest.onboardexperience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class OnboardExperienceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnboardExperienceApplication.class, args);
	}

}
