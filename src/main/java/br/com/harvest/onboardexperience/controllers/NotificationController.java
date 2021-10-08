package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.infra.notification.dtos.NotificationForm;
import br.com.harvest.onboardexperience.infra.notification.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Notifications")
@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*", maxAge = 36000)
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(description = "Salva uma notificação no banco de dados.")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MASTER')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/notification")
    public void sendNotification(@Valid @RequestBody NotificationForm form) {
        notificationService.sendNotification(form);
    }

}
