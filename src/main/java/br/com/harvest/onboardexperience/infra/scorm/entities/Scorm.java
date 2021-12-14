package br.com.harvest.onboardexperience.infra.scorm.entities;

import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.Client;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
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
    @JoinColumn(name = "idclient")
    private Client client;

    @OneToMany(mappedBy = "scorm")
    private Set<ScormRegistration> registrations;

}
