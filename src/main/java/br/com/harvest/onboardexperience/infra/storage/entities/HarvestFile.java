package br.com.harvest.onboardexperience.infra.storage.entities;

import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.*;
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

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "file_path")
    private String contentPath;

    @ManyToMany
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

}
