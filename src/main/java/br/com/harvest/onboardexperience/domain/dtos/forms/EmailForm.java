package br.com.harvest.onboardexperience.domain.dtos.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailForm {

    @NotBlank
    private String email;

}
