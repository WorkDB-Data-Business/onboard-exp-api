package br.com.harvest.onboardexperience.infra.scorm.entities;

import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.Client;
import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbscorm")
public class Scorm extends BaseEntityAudit {

    @Id
    @Column(name = "idscorm")
    private String id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;

}
