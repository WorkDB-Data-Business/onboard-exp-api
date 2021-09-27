package br.com.harvest.onboardexperience.infra.storage.dtos;

import br.com.harvest.onboardexperience.domain.dtos.ClientSimpleDto;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileDto {

    private Long id;

    private String name;

    private List<ClientSimpleDto> authorizedClients;

    private String contentId;

    private long contentLength;

    private String mimeType;

}
