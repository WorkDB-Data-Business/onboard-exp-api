package br.com.harvest.onboardexperience.infra.scorm.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
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
public class ScormCourseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("created")
    private OffsetDateTime created;

    @JsonProperty("updated")
    private OffsetDateTime updated;

    @JsonProperty("version")
    private Integer version;

    @JsonProperty("registrationCount")
    private Integer registrationCount;

    @JsonProperty("courseLearningStandard")
    private CourseSchema.CourseLearningStandardEnum courseLearningStandard;

    @JsonProperty("tags")
    private List<String> tags;

}
