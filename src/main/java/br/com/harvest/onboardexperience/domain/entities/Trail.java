package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbtrail")
@EqualsAndHashCode(callSuper = true)
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

//
//    @ManyToMany
//    @JoinTable(
//            name = "tbtrail_stage",
//            joinColumns = @JoinColumn(name = "idtrail"),
//            inverseJoinColumns = @JoinColumn(name = "idstage"))
//    private List<Stage> stages;

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

//    @ManyToMany
//    @JoinTable(
//            name = "tbtrail_user_registration",
//            joinColumns = @JoinColumn(name = "idtrail"),
//            inverseJoinColumns = @JoinColumn(name = "iduser"))
//    private List<UserTrailRegistration> trailRegistrations;
//
}
