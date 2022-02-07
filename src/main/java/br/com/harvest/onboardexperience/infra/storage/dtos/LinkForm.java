package br.com.harvest.onboardexperience.infra.storage.dtos;


import br.com.harvest.onboardexperience.infra.storage.enumerators.ContentType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinkForm {

    @NotBlank
    private String link;

    @NotNull
    private ContentType contentType;

    @Builder.Default
    private Boolean isActive = true;

}
