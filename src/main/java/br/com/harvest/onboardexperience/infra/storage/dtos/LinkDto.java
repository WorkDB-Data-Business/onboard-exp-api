package br.com.harvest.onboardexperience.infra.storage.dtos;

import br.com.harvest.onboardexperience.domain.dtos.ClientSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.enumerators.ContentType;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinkDto {

    private Long id;

    private String description;

    private List<ClientSimpleDto> authorizedClients;

    private String link;

    private ContentType contentType;

    private Boolean isActive;

}
