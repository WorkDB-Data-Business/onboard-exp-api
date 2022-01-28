package br.com.harvest.onboardexperience.infra.storage.entities;


import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.infra.storage.enumerators.ContentType;
import br.com.harvest.onboardexperience.utils.SQLQueryUtils;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@Data
@Builder
@Entity(name = "tblink")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_LINK, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
public class Link extends BaseEntityAudit {

    @Id
    @Column(name = "idlink")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @Column(name = "preview_image_path")
    private String previewImagePath;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @ManyToMany
    @JoinTable(
            name = "tbharvest_library_link",
            joinColumns = @JoinColumn(name = "idlink"),
            inverseJoinColumns = @JoinColumn(name = "idclient"))
    private List<Client> authorizedClients;

    @Column(name = "is_active")
    private Boolean isActive;

}
