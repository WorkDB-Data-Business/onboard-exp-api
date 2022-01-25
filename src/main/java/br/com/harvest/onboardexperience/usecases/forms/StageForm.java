package br.com.harvest.onboardexperience.usecases.forms;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StageForm {

    @NotNull
    private Long userId;

    @NotNull
    private Long stageId;

}
