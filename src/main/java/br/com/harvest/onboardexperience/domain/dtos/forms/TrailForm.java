package br.com.harvest.onboardexperience.domain.dtos.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrailForm {

    @JsonProperty("name")
    private String nameTrail;

    @JsonProperty("des")
    private String descricaoTrilha;
}
