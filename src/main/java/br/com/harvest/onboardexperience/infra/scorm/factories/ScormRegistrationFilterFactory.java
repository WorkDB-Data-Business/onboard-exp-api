package br.com.harvest.onboardexperience.infra.scorm.factories;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.infra.scorm.filters.ScormRegistrationFilter;
import lombok.NonNull;

public class ScormRegistrationFilterFactory {

    public static ScormRegistrationFilter filterByUser(@NonNull User user){
        return ScormRegistrationFilter.builder()
                .learnerId(user.getScormLearnerId())
                .build();
    }

    public static ScormRegistrationFilter filterByCourse(@NonNull Scorm course){
        return ScormRegistrationFilter
                .builder()
                .courseId(course.getId())
                .build();
    }

    public static ScormRegistrationFilter filterByCourseAndUser(@NonNull Scorm course, @NonNull User user){
        return ScormRegistrationFilter
                .builder()
                .courseId(course.getId())
                .learnerId(user.getScormLearnerId())
                .build();
    }
}
