package br.com.harvest.onboardexperience.domain.entities;


import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "description")
    private String description;

    @Column(name = "score_question")
    private Long scoreQuestion;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_multiplechoice")
    private Boolean isMultipleChoice;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerQuestion> answersQuestions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tbquestion_client",
            joinColumns = @JoinColumn(name = "idquestion"),
            inverseJoinColumns = @JoinColumn(name = "idclient"))
    private List<Client>authorizedClients;


    public void addAnswer(List<AnswerQuestion> answers){
        if(!ObjectUtils.isEmpty(answers)){
            answers.forEach(answer -> answer.setQuestion(this));
        }
    }

    public void addAnswer(AnswerQuestion answer){
        if(Objects.nonNull(answer)){
            answer.setQuestion(this);
        }
    }

}
