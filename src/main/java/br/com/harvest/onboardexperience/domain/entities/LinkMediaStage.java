package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.domain.entities.keys.LinkMediaStageId;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@IdClass(LinkMediaStageId.class)
@Entity(name="tbstage_link")
public class LinkMediaStage extends BaseEntityAudit{

    @Id
    @ManyToOne
    @JoinColumn(name = "idlink")
    private Link link;

    @Id
    @ManyToOne
    @JoinColumn(name = "idstage")
    private Stage stage;

}
