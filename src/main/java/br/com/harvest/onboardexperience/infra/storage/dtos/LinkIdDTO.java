package br.com.harvest.onboardexperience.infra.storage.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LinkIdDTO {

    @JsonProperty("id")
    private Long id;

}
