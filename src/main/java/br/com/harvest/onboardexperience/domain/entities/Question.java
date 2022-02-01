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
    @Column(name = "id_question")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
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

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerQuestion> answers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tbharvest_question_answer",
            joinColumns = @JoinColumn(name = "id_question"),
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
