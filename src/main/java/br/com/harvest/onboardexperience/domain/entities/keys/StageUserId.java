package br.com.harvest.onboardexperience.domain.entities.keys;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StageUserId implements Serializable {

    private Long user;

    private Long stage;

}
