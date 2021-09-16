package br.com.harvest.onboardexperience.domain.entities;

import br.com.harvest.onboardexperience.utils.SQLQueryUtils;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbgroup", schema="public")
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_GROUP, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
public class Group extends BaseEntityAudit {

    private static final long serialVersionUID = 7476491148425775323L;

    @Builder
    public Group(String createdBy, String updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt,
                 Long id, String name, Boolean isActive, Client client, List<User> users, List<CompanyRole> companyRoles) {
        super(createdBy, updatedBy, createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.client = client;
        this.users = users;
        this.companyRoles = companyRoles;
    }

    @Id
    @Column(name = "idgroup")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tbgroup_user",
            joinColumns = @JoinColumn(name = "idgroup"),
            inverseJoinColumns = @JoinColumn(name = "iduser"))
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "tbgroup_company_role",
            joinColumns = @JoinColumn(name = "idgroup"),
            inverseJoinColumns = @JoinColumn(name = "idcompany_role"))
    private List<CompanyRole> companyRoles;

}
