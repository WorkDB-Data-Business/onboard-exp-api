package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MediaExecution {

    @JsonProperty("id")
    private String id;

    @JsonProperty("storage")
    private Storage storage;

    @JsonProperty("contentPath")
    private String contentPath;

    @JsonProperty("isCompleted")
    private Boolean isCompleted;

    @JsonProperty("completedAt")
    private LocalDateTime completedAt;

    @JsonProperty("previewImagePath")
    private String previewImagePath;

    @JsonProperty("name")
    private String name;

    @JsonIgnore
    private LocalDateTime creationDate;

}
