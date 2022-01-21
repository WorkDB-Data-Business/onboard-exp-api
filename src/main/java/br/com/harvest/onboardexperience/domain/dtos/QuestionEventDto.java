package br.com.harvest.onboardexperience.domain.dtos;

import br.com.harvest.onboardexperience.domain.entities.Event;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEventDto {

    private Long id;

    private String name;

    private String descripton;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isMultipleChoice = false;

    @Builder.Default
    private Boolean isDescriptive = false;
    private Event event;
}

