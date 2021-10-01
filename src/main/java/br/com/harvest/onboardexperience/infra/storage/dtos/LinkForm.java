package br.com.harvest.onboardexperience.infra.storage.dtos;


import br.com.harvest.onboardexperience.infra.storage.enumerators.ContentType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinkForm {

    private Long id;

    @Size(min = 5)
    @NotBlank
    private String description;

    @NotBlank
    private String link;

    @NotNull
    private ContentType contentType;

    @Builder.Default
    private Boolean isActive = true;

}
