package br.com.harvest.onboardexperience.infra.storage.dtos;

import br.com.harvest.onboardexperience.infra.storage.enumerators.ContentType;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinkSimpleDto {

    private Long id;

    private String description;

    private String link;

    private ContentType contentType;

    private Boolean isActive;

}
