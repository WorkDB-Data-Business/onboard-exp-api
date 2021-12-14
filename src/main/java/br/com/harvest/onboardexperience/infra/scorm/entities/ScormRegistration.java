package br.com.harvest.onboardexperience.infra.scorm.entities;

import br.com.harvest.onboardexperience.domain.entities.BaseEntityAudit;
import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbscorm_registration")
public class ScormRegistration extends BaseEntityAudit {

    @Id
    @Column(name = "idscorm_registration")
    private String id;

    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idscorm")
    private Scorm scorm;

    @Builder.Default
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScormRegistration that = (ScormRegistration) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(isCompleted, that.isCompleted) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, isCompleted, isActive);
    }
}
