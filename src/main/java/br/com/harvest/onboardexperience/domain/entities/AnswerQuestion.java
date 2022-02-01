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
@Table(name = "tbanswer_question", schema = "public")
public class AnswerQuestion {

    @Id
    @Column(name = "idanswer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer")
    private String answer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idquestion")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "is_correct")
    private Boolean isCorrect;
}
