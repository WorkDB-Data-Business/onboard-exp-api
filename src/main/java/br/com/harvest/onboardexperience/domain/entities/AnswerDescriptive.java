package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbanswer_descriptive", schema = "public")
public class AnswerDescriptive {

    @Id
    @Column(name = "idanswer_descriptive")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idquestion")
    private Question idQuestion;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "answer_descriptive")
    private String answerDescriptive;
}
