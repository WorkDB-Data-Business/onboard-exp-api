package br.com.harvest.onboardexperience.infra.storage.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class HarvestLibraryFilter {

        @JsonProperty("criteriaFilter")
    private String criteriaFilter;

    @JsonProperty("courseLearningStandard")
    private CourseSchema.CourseLearningStandardEnum courseLearningStandard;

}
