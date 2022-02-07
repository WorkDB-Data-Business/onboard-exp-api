package br.com.harvest.onboardexperience.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbtext", schema = "public")
public class Text {

    @Id
    @Column(name = "idtext")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "text")
    private String text;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "idevent")
    private Event event;

    @ManyToMany
    @JoinTable(
            name = "tbtext_client",
            joinColumns = @JoinColumn(name = "idtext"),
            inverseJoinColumns = @JoinColumn(name = "idclient"))
    private List<Client> authorizedClients;


}
