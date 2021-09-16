package br.com.harvest.onboardexperience.domain.dtos.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWelcomeForm implements Serializable {

    private static final long serialVersionUID = -3193462244390226870L;

    private Long id;

    @Size(min = 3)
    @NotBlank
    private String nickname;

    @NotNull
    private Integer idAvatar;

}
