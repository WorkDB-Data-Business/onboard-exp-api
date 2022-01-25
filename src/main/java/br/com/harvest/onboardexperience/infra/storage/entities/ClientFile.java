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

@Entity(name = "tbclient_file")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClientFile extends BaseEntityAudit {
    @Id
    @Column(name = "idclient_file")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "file_path")
    private String contentPath;

      @ContentId
    private String contentId;

    @ContentLength
    private Long contentLength;

    @MimeType
    private String mimeType;


}
