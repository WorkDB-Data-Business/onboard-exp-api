package br.com.harvest.onboardexperience.infra.scorm.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScormDto {

    private String id;

    private String title;

}
