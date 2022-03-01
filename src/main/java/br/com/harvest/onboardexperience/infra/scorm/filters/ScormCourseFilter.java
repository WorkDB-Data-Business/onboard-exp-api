package br.com.harvest.onboardexperience.infra.scorm.filters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScormCourseFilter {

    @JsonProperty(namespace = "since")
    private OffsetDateTime since;

    @JsonProperty(namespace = "until")
    private OffsetDateTime until;

    @JsonIgnore
    private String datetimeFilter;

    @JsonIgnore
    private List<String> tags;

    @JsonIgnore
    private String filter;

    @JsonIgnore
    private String filterBy;

    @JsonProperty(namespace = "orderBy")
    private String orderBy;

    @JsonIgnore
    private String more;

    @JsonIgnore
    private Boolean includeCourseMetadata;

    @JsonIgnore
    private Boolean includeRegistrationCount;

}
