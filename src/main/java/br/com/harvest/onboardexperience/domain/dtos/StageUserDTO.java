package br.com.harvest.onboardexperience.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StageUserDTO {

    @JsonProperty("user")
    private UserSimpleDto user;

    @JsonProperty("score")
    private BigDecimal score;

    @JsonProperty("isCompleted")
    private Boolean isCompleted;

    @JsonProperty("completedAt")
    private LocalDateTime completedAt;

    @JsonProperty("startedAt")
    private LocalDateTime startedAt;

    @JsonProperty("scorms")
    private List<ScormMediaUserDTO> scorms;

    @JsonProperty("harvestFiles")
    private List<HarvestFileMediaUserDTO> harvestFiles;

    @JsonProperty("links")
    private List<LinkMediaUserDTO> links;

}
