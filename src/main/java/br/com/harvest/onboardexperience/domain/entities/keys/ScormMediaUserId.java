package br.com.harvest.onboardexperience.domain.entities.keys;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScormMediaUserId implements Serializable {

    private ScormMediaStageId scormMedia;

    private Long user;

}
