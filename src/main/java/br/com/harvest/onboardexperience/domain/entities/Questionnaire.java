package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tbquestionnaire", schema = "public")
public class Questionnaire {

    @Id
    @Column(name = "idquestionnaire")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "preview_image_path")
    private String previewImagePath;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionnaire")
    private List<Question> questionOfQuestionnaire = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tbquestionnaire_client",
            joinColumns = @JoinColumn(name = "idquestionnaire"),
            inverseJoinColumns = @JoinColumn(name = "idclient"))
    private List<Client> authorizedClients;

}


