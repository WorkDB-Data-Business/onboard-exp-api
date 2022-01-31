package br.com.harvest.onboardexperience.infra.storage.entities;


import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.utils.SQLQueryUtils;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;

@Entity
@Table(name = "tbfile")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Image extends BaseEntityAudit {

    @Id
    @Column(name = "idfile")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

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

    @Builder.Default
    @Column(name = "is_image_preview")
    private Boolean isImagePreview = true;

}
