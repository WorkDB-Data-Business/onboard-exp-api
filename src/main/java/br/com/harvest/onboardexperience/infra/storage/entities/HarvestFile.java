package br.com.harvest.onboardexperience.infra.storage.entities;

import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.utils.SQLQueryUtils;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;
import java.util.List;

@Entity(name = "tbfile")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HarvestFile extends BaseEntityAudit {

    @Id
    @Column(name = "idfile")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preview_image_path")
    private String previewImagePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "file_path")
    private String contentPath;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tbharvest_library_file",
            joinColumns = @JoinColumn(name = "idfile"),
            inverseJoinColumns = @JoinColumn(name = "idclient"))
    private List<Client> authorizedClients;

    @ContentId
    private String contentId;

    @ContentLength
    private Long contentLength;

    @MimeType
    private String mimeType;

    @Builder.Default
    @Column(name = "is_asset")
    private Boolean isAsset = false;

}
