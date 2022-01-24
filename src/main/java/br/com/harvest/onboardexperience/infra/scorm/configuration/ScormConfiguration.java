package br.com.harvest.onboardexperience.infra.scorm.configuration;

import br.com.harvest.onboardexperience.utils.Constants;
import com.rusticisoftware.cloud.v2.client.Configuration;
import com.rusticisoftware.cloud.v2.client.api.CourseApi;
import com.rusticisoftware.cloud.v2.client.api.LearnerApi;
import com.rusticisoftware.cloud.v2.client.api.RegistrationApi;
import com.rusticisoftware.cloud.v2.client.auth.HttpBasicAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ScormConfiguration {

    @Value(Constants.Harvest.ScormCloud.APP_ID)
    private String APP_ID;

    @Value(Constants.Harvest.ScormCloud.SECRET_KEY)
    private String SECRET_KEY;

    private final String AUTHENTICATION_TYPE = "APP_NORMAL";

    @Bean
    public HttpBasicAuth configureScormAuth(){
        HttpBasicAuth auth = (HttpBasicAuth) Configuration.getDefaultApiClient().getAuthentication(AUTHENTICATION_TYPE);

        auth.setUsername(APP_ID);
        auth.setPassword(SECRET_KEY);
        return auth;
    }

    @Bean
    public CourseApi configureCourseApi(){
        return new CourseApi();
    }

    @Bean
    public RegistrationApi configureRegistrationApi(){
        return new RegistrationApi();
    }

    @Bean
    public LearnerApi configureLearnerApi(){
        return new LearnerApi();
    }
}
