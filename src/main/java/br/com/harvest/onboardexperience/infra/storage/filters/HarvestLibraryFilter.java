package br.com.harvest.onboardexperience.infra.storage.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class HarvestLibraryFilter extends CustomFilter {

    @JsonProperty("courseLearningStandard")
    private CourseSchema.CourseLearningStandardEnum courseLearningStandard;

}
