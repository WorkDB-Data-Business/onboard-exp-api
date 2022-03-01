package br.com.harvest.onboardexperience.infra.storage.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class CustomFilter {

    @JsonProperty("customFilter")
    protected String customFilter;

}
