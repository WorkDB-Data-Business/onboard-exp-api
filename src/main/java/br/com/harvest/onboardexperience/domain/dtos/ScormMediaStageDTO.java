package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScormMediaStageDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("executionOrder")
    private Integer executionOrder;

}
