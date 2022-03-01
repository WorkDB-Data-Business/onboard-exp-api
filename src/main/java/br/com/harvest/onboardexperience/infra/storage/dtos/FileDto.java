package br.com.harvest.onboardexperience.infra.storage.dtos;

import br.com.harvest.onboardexperience.domain.dtos.UserSimpleDto;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private UserSimpleDto author;

    private List<Long> authorizedClientsId;

    private String fileEncoded;

    private String previewImagePath;

    private String description;

    private Long contentLength;

    private String mimeType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Storage storage;

}
