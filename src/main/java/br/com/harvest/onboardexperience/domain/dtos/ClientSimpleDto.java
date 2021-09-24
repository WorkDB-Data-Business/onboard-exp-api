package br.com.harvest.onboardexperience.domain.dtos;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClientSimpleDto {

    private Long id;

    private String cnpj;

    private String name;

}
