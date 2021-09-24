package br.com.harvest.onboardexperience.infra.storage.dtos;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileSimpleDto {

    private String name;

    private String contentId;

    private long contentLength;

    private String mimeType;

}
