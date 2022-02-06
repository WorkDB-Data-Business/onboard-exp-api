package br.com.harvest.onboardexperience.domain.entities.keys;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkMediaUserId implements Serializable {

    private Long link;

    private Long stage;

    private Long user;

}
