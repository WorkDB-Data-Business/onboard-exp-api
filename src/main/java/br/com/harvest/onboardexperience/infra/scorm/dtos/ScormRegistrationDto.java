package br.com.harvest.onboardexperience.infra.scorm.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rusticisoftware.cloud.v2.client.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScormRegistrationDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("updated")
    private OffsetDateTime updated;

    @JsonProperty("registrationCompletion")
    private RegistrationCompletion registrationCompletion;

    @JsonProperty("registrationCompletionAmount")
    private Double registrationCompletionAmount;

    @JsonProperty("registrationSuccess")
    private RegistrationSuccess registrationSuccess;

    @JsonProperty("score")
    private ScoreSchema score;

    @JsonProperty("totalSecondsTracked")
    private Double totalSecondsTracked;

    @JsonProperty("firstAccessDate")
    private OffsetDateTime firstAccessDate;

    @JsonProperty("lastAccessDate")
    private OffsetDateTime lastAccessDate;

    @JsonProperty("completedDate")
    private OffsetDateTime completedDate;

    @JsonProperty("createdDate")
    private OffsetDateTime createdDate;

    @JsonProperty("course")
    private CourseReferenceSchema course;

    @JsonProperty("learner")
    private LearnerSchema learner;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("globalObjectives")
    private List<ObjectiveSchema> globalObjectives;

    @JsonProperty("activityDetails")
    private ActivityResultSchema activityDetails;

}
