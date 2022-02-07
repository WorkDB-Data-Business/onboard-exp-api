package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    @NotNull
    private String title;

    @JsonProperty("description")
    @NotNull
    private String description;

    @JsonProperty("text")
    @NotNull
    private String text;

    @Builder.Default
    private Boolean isActive = true;

    @JsonProperty
    private List<Client> authorizedClients;


}

