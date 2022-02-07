package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;

import javax.persistence.*;

@Data
@Builder
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

    @ManyToOne()
    @JoinColumn(name = "idquestion")
    private Question question;


    @Column(name = "is_correct")
    private Boolean isCorrect;
}
