package br.com.harvest.onboardexperience.domain.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GroupSimpleDto {

    private Long id;

    private String name;

    private Boolean isActive;

}
