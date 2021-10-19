package br.com.harvest.onboardexperience.infra.storage.dtos;

import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileSimpleDto {

    private Long id;

    private String name;

    private String contentId;

    private Long contentLength;

    private String mimeType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Storage storage;

}
