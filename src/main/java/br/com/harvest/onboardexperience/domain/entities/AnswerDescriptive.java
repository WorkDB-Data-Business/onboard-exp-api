package br.com.harvest.onboardexperience.domain.entities;


import br.com.harvest.onboardexperience.domain.dtos.AnswerQuestionDto;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "tbanswer_descriptive", schema = "public")
public class AnswerDescriptive {

    @Id
    @Column(name = "id_answer_descriptive")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_question")
    private Question idQuestion;

    @ManyToOne
    @JoinColumn(name = "iduser")
    private User iduser;

    @Column(name = "answer_descriptive")
    private String answerDescriptive;
}
