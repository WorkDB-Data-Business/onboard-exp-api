package br.com.harvest.onboardexperience.infra.notification.dtos;

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

    private LocalDateTime sentAt;

}
