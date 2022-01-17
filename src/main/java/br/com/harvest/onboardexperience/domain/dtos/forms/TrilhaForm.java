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
public class TrilhaForm {

    @JsonProperty("nomeTrilha")
    private String nomeTrilha;

    @JsonProperty("descricaoTrilha")
    private String descricaoTrilha;
}
