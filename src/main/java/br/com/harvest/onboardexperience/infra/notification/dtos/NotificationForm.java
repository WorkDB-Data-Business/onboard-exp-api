package br.com.harvest.onboardexperience.infra.notification.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NotificationForm {

    @NotBlank
    private String text;

    private List<Long> clients;

    private List<Long> users;

    private Long author;

}
