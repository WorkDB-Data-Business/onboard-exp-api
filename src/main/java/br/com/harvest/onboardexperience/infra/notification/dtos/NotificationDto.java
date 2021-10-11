package br.com.harvest.onboardexperience.infra.notification.dtos;

import br.com.harvest.onboardexperience.domain.dtos.UserSimpleDto;
import br.com.harvest.onboardexperience.domain.entities.User;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NotificationDto {

    private Long id;

    private String text;

    private Boolean wasVisualized;

    private UserSimpleDto author;

    private LocalDateTime sentAt;

}
