package br.com.harvest.onboardexperience.domain.dtos.forms;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {


    //	@Pattern(regexp = RegexUtils.PASSWORD_VALIDATION) TODO: Commented line to facilitate testing, uncomment when it's necessary
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
