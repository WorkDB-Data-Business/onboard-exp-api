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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbtrail")
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = SQLQueryUtils.SOFT_DELETE_TRAIL, check = ResultCheckStyle.COUNT)
@Where(clause = SQLQueryUtils.IS_ACTIVE_FILTER)
public class Trail extends BaseEntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtrail")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "map_image_path")
    private String mapImagePath;

    @Column(name = "map_music_path")
    private String mapMusicPath;

    @Column(name = "conclusion_date")
    private LocalDateTime conclusionDate;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "idcoin")
    private Coin coin;

    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tbtrail_map_position_path",
            joinColumns = @JoinColumn(name = "idtrail"),
            inverseJoinColumns = {@JoinColumn(name = "x_axis"), @JoinColumn(name = "y_axis")})
    private List<Position> characterMapPositionPath;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tbtrail_group",
            joinColumns = @JoinColumn(name = "idtrail"),
            inverseJoinColumns = @JoinColumn(name = "idgroup"))
    private List<Group> groups;

    @OneToMany(mappedBy = "trail")
    private List<UserTrailRegistration> trailRegistrations;

}
