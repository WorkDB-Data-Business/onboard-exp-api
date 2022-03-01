package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionnaireSimpleDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("imagePreviewEncoded")
    private String imagePreviewEncoded;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("storage")
    private Storage storage;

}
