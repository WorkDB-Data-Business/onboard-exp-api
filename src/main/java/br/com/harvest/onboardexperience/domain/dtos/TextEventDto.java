package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.Event;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextEventDto {

    private Long id;

    private String title;

    private String descripton;

    private String text;

    @Builder.Default
    private Boolean isActive = true;


}
