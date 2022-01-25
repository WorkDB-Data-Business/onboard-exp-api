package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrailDTO {

    private Long id;

    private String nameTRail;

    private String arquivoTrilhaNome;

    private byte[] arquivoTrilhaBytes;

    private String descriotionTrail;

    private User userCreatedTrail;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isAvailable = false;
}
