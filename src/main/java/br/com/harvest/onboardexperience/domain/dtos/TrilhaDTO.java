package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrilhaDTO {

    private Long id;
    private String nomeTrilha;
    private String arquivoTrilhaNome;
    private byte[] arquivoTrilhaBytes;
    private String descricaoTrilha;
    private User userCreatedTrilha;
}
