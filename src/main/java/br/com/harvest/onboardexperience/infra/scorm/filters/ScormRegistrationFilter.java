package br.com.harvest.onboardexperience.infra.scorm.filters;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScormRegistrationFilter {

    private String courseId;
    private String learnerId;
    private OffsetDateTime since;
    private OffsetDateTime until;
    private String datetimeFilter;
    private List<String> tags;
    private String filter;
    private String filterBy;
    private String orderBy;
    private Boolean includeChildResults;
    private Boolean includeInteractionsAndObjectives;
    private Boolean includeRuntime;

}
