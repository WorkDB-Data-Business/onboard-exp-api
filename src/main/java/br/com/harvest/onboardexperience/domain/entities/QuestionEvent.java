package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tbquestion", schema = "public")
public class QuestionEvent {

    @Id
    @Column(name = "id_question")
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

    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;

    @Builder.Default
    @OneToMany(mappedBy = "questionEvent", cascade = CascadeType.ALL)
    private List<AnswerQuestion> answers = new ArrayList<>();

}
