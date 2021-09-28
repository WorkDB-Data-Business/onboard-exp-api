package br.com.harvest.onboardexperience.infra.storage.dtos;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileSimpleDto {

    private Long id;

    private String name;

    private String contentId;

    private Long contentLength;

    private String mimeType;

}
