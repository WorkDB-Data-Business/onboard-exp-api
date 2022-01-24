package br.com.harvest.onboardexperience.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbquestion", schema = "public")
public class QuestionEvent {

    @Id
    @Column(name = "idquestion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "descripton")
    private String descripton;

    @Column(name = "note_question")
    private Long noteQuestion;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_multiplechoice")
    private Boolean isMultipleChoice;

    @Column(name = "is_descriptive")
    private Boolean isDescriptive;

    @ManyToOne
    @JoinColumn(name = "idevent")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;


}
