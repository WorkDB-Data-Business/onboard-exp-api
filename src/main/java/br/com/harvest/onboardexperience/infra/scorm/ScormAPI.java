

package br.com.harvest.onboardexperience.infra.scorm;

import br.com.harvest.onboardexperience.infra.scorm.factories.ScormRegistrationFactory;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import com.rusticisoftware.cloud.v2.client.ApiException;
import com.rusticisoftware.cloud.v2.client.api.CourseApi;
import com.rusticisoftware.cloud.v2.client.api.RegistrationApi;
import com.rusticisoftware.cloud.v2.client.model.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class ScormAPI {

    @Autowired
    private CourseApi courseApi;

    @Autowired
    private RegistrationApi registrationApi;

    private ImportJobResultSchema proccessCourse(CourseApi courseApi, StringResultSchema jobId) throws ApiException {
        ImportJobResultSchema job = courseApi.getImportJobStatus(jobId.getResult());

        while(job.getStatus().equals(ImportJobResultSchema.StatusEnum.RUNNING)) {
            switch (job.getStatus()) {
                case RUNNING: {
                    GenericUtils.sleep(TimeUnit.SECONDS, 1);
                    break;
                }
                case ERROR: {
                    throw new IllegalArgumentException("Course is not properly formatted: " + job.getMessage());
                }
            }
            job = courseApi.getImportJobStatus(jobId.getResult());
        }
        return job;
    }

    public CourseSchema createCourse(String courseId, File file) throws ApiException {
        StringResultSchema jobId = courseApi.createUploadAndImportCourseJob(courseId, null, null,
                null, null, file);

        return proccessCourse(courseApi, jobId).getImportResult().getCourse();
    }

    public void createRegistration(String courseId, LearnerSchema learner, String registrationId) throws ApiException {
        registrationApi.createRegistration(ScormRegistrationFactory.createRegistrationSchema(courseId, learner, registrationId), null);
    }

    private LearnerSchema createLearnerSchema(@NonNull String learnerId,
                                              @NonNull String email,
                                              @NonNull String firstName,
                                              @NonNull String lastName){
        return new LearnerSchema()
                .id(learnerId)
                .email(email)
                .firstName(firstName)
                .lastName(lastName);
    }

    public String buildLaunchLink(String registrationId) throws ApiException {
        return registrationApi.buildRegistrationLaunchLink(registrationId,
                new LaunchLinkRequestSchema().redirectOnExitUrl("Message")).getLaunchLink();
    }

    public RegistrationSchema getResultForRegistration(String registrationId) throws ApiException {
        return registrationApi.getRegistrationProgress(registrationId, null, null,
                null);
    }

    public List<CourseSchema> getAllCourses() throws ApiException {

        CourseListSchema response = courseApi.getCourses(null, null, null, null, null,
                null, null, null, null, null);

        List<CourseSchema> courseList = response.getCourses();

        while (response.getMore() != null) {
            response = courseApi.getCourses(null, null, null, null, null, null,
                    null, response.getMore(), null, null);
            courseList.addAll(response.getCourses());
        }

        return courseList;
    }

    public List<RegistrationSchema> getAllRegistrations() throws ApiException {
        RegistrationListSchema response = registrationApi.getRegistrations(null, null, null, null,
                null, null, null, null, null, null, null,
                null, null);

        List<RegistrationSchema> registrationList = response.getRegistrations();
        while (response.getMore() != null) {
            response = registrationApi.getRegistrations(null, null, null, null, null,
                    null, null, null, null, response.getMore(), null,
                    null, null);
            registrationList.addAll(response.getRegistrations());
        }

        return registrationList;
    }

    public void deleteRegistration(String registrationId) throws ApiException {
        registrationApi.deleteRegistration(registrationId);
    }

    public void deleteCourse(@NonNull String courseId) throws ApiException {
        courseApi.deleteCourse(courseId);
    }

}
