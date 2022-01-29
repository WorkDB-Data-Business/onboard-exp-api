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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbanswer_question",
            joinColumns = @JoinColumn(name = "id_question"),
            inverseJoinColumns = @JoinColumn(name = "id_answer"))
    private List<AnswerQuestion> answers;

}
