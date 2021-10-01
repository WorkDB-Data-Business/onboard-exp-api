package br.com.harvest.onboardexperience.infra.storage.dtos;

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

    private List<Long> authorizedClientsId;

    private String fileEncoded;

    private Long contentLength;

    private String mimeType;

}
