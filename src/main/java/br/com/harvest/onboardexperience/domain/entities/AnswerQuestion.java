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
@Table(name = "tbanswerquestion", schema = "public")
public class AnswerQuestion {

    @Id
    @Column(name = "id_answer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer")
    private String answer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_question")
    private Question question;

    @Column(name = "is_correct")
    private Boolean isCorrect;
}
