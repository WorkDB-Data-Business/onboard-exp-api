package br.com.harvest.onboardexperience.infra.scorm.entities;

import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.utils.SQLQueryUtils;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbscorm")
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_SCORM, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
public class Scorm extends BaseEntityAudit {

    @Id
    @Column(name = "idscorm")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "preview_image_path")
    private String previewImagePath;

    @Column(name = "created_on_scorm_cloud")
    private OffsetDateTime created;

    @Column(name = "updated_on_scorm_cloud")
    private OffsetDateTime updated;

    @Column(name = "version")
    private Integer version;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_learning_standard")
    private CourseSchema.CourseLearningStandardEnum courseLearningStandard;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Builder.Default
    @OneToMany(mappedBy = "scorm")
    private Set<ScormRegistration> registrations = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "tbharvest_library_scorm",
            joinColumns = @JoinColumn(name = "idscorm"),
            inverseJoinColumns = @JoinColumn(name = "idclient"))
    private List<Client> authorizedClients;

}
