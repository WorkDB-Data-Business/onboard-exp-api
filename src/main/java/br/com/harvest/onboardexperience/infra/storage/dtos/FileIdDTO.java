package br.com.harvest.onboardexperience.infra.storage.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileIdDTO {

    @JsonProperty("id")
    private Long id;

}
