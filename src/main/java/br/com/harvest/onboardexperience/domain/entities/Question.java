package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tbquestion", schema = "public")
public class Question {

    @Id
    @Column(name = "idquestion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "score_question")
    private BigDecimal scoreQuestion;

    @Column(name = "is_multiple_choice")
    private Boolean isMultipleChoice;

    @ManyToOne
    @JoinColumn(name = "idquestionnaire")
    private Questionnaire questionnaire;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    private List<AnswerQuestion> answersQuestions = new ArrayList<>();

}
