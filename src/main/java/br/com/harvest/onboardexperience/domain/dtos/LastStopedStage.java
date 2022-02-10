package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LastStopedStage {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

}
