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
public class TrailDTO {

    private Long id;
    private String nameTRail;
    private String arquivoTrilhaNome;
    private byte[] arquivoTrilhaBytes;
    private String descriotionTrail;
    private User userCreatedTrail;
}
