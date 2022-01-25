

package br.com.harvest.onboardexperience.infra.scorm;

import br.com.harvest.onboardexperience.infra.scorm.filters.ScormCourseFilter;
import br.com.harvest.onboardexperience.infra.scorm.filters.ScormRegistrationFilter;
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
import java.util.Objects;
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

    public void createRegistration(CreateRegistrationSchema schema) throws ApiException {
        registrationApi.createRegistration(schema, null);
    }

    public String buildLaunchLink(String registrationId) throws ApiException {
        return registrationApi.buildRegistrationLaunchLink(registrationId,
                new LaunchLinkRequestSchema().redirectOnExitUrl("Message")).getLaunchLink();
    }

    public RegistrationSchema getResultForRegistration(String registrationId) throws ApiException {
        return registrationApi.getRegistrationProgress(registrationId, null, null,
                null);
    }

    public List<CourseSchema> getAllCourses(@NonNull ScormCourseFilter filter) throws ApiException {

        CourseListSchema response = courseApi.getCourses(filter.getSince(), filter.getUntil(), filter.getDatetimeFilter(),
                filter.getTags(), filter.getFilter(), filter.getFilterBy(), filter.getOrderBy(), null,
                filter.getIncludeCourseMetadata(), filter.getIncludeRegistrationCount());

        List<CourseSchema> courseList = response.getCourses();

        while (Objects.nonNull(response.getMore())) {
            response = courseApi.getCourses(filter.getSince(), filter.getUntil(), null,
                    filter.getTags(), null,filter.getFilterBy(), filter.getOrderBy(), response.getMore(),
                    true, true);
            courseList.addAll(response.getCourses());
        }

        return courseList;
    }

    public CourseSchema getCourse(@NonNull String courseId) throws ApiException {
        return courseApi.getCourse(courseId, true, true);
    }

    public List<RegistrationSchema> getAllRegistrations(@NonNull ScormRegistrationFilter filter) throws ApiException {
        RegistrationListSchema response = registrationApi.getRegistrations(filter.getCourseId(), filter.getLearnerId(),
                filter.getSince(), filter.getUntil(),filter.getDatetimeFilter(), filter.getTags(),
                filter.getFilter(), filter.getFilterBy(), filter.getOrderBy(), null, filter.getIncludeChildResults(),
                filter.getIncludeInteractionsAndObjectives(), filter.getIncludeRuntime());

        List<RegistrationSchema> registrationList = response.getRegistrations();
        while (Objects.nonNull(response.getMore())) {
            response = registrationApi.getRegistrations(filter.getCourseId(), filter.getLearnerId(),
                    filter.getSince(), filter.getUntil(),filter.getDatetimeFilter(), filter.getTags(),
                    filter.getFilter(), filter.getFilterBy(), filter.getOrderBy(), response.getMore(), filter.getIncludeChildResults(),
                    filter.getIncludeInteractionsAndObjectives(), filter.getIncludeRuntime());
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
