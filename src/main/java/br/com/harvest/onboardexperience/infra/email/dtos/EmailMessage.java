package br.com.harvest.onboardexperience.infra.email.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailMessage {

    private Set<String> receivers;

    @NotBlank
    private String subject;

    @NotBlank
    private String model;

    private Map<String, Object> variables;

}
