package br.com.harvest.onboardexperience.domain.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDescriptiveDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("answer_descriptive")
    private String answerDescriptive;





}
