package br.com.harvest.onboardexperience.infra.storage.dtos;

import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScormSimpleDTO {

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

    @JsonProperty("storage")
    private Storage storage;

}
