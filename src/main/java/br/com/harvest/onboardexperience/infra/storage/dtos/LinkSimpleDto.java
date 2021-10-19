package br.com.harvest.onboardexperience.infra.storage.dtos;

import br.com.harvest.onboardexperience.infra.storage.enumerators.ContentType;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinkSimpleDto {

    private Long id;

    private String description;

    private String link;

    private ContentType contentType;

    private Boolean isActive;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Storage storage;

}
