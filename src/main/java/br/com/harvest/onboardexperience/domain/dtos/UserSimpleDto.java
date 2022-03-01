package br.com.harvest.onboardexperience.domain.dtos;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserSimpleDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}
