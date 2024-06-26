package br.com.harvest.onboardexperience.infra.scorm.factories;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import com.rusticisoftware.cloud.v2.client.model.CreateRegistrationSchema;
import com.rusticisoftware.cloud.v2.client.model.LearnerSchema;
import lombok.NonNull;

public class ScormRegistrationFactory {

    private ScormRegistrationFactory() {}

    public static CreateRegistrationSchema createRegistrationSchema(@NonNull String courseId, @NonNull LearnerSchema learner,
                                                              @NonNull String registrationId){
        return new CreateRegistrationSchema()
                .courseId(courseId)
                .learner(learner)
                .registrationId(registrationId);
    }

    public static LearnerSchema createLearnerSchema(@NonNull String learnerId, @NonNull String email, @NonNull String firstName,
                                              @NonNull String lastName){
        return new LearnerSchema()
                .id(learnerId)
                .email(email)
                .firstName(firstName)
                .lastName(lastName);
    }

    public static CreateRegistrationSchema createRegistrationSchema(@NonNull Scorm scormCourse, @NonNull User user,
                                                                    @NonNull String registrationId){
        return new CreateRegistrationSchema()
                .courseId(scormCourse.getId())
                .learner(createLearnerSchema(user))
                .registrationId(registrationId);
    }

    public static LearnerSchema createLearnerSchema(@NonNull User user){
        return new LearnerSchema()
                .id(user.getScormLearnerId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName());
    }
}
