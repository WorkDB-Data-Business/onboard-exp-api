package br.com.harvest.onboardexperience.infra.scorm.dtos;

import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScormDto {

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

    @JsonIgnore
    private Set<ScormRegistrationDto> registrations;

    public Integer getRegistrationCount() {
        if(ObjectUtils.isEmpty(this.registrations)){
            return 0;
        }
        return this.registrations.size();
    }
}
