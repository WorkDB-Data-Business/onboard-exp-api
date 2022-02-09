package br.com.harvest.onboardexperience.controllers;


import br.com.harvest.onboardexperience.domain.dtos.DashboardMasterMetricsDTO;
import br.com.harvest.onboardexperience.services.DashboardService;
import com.rusticisoftware.cloud.v2.client.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Group")
@RestController
@RequestMapping("/v1/dashboard")
@CrossOrigin(origins = "*", maxAge = 36000)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(description = "Retorna as métricas para o dashboard do usuário master.")
    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardMasterMetricsDTO> find() throws ApiException {
        return ResponseEntity.ok(dashboardService.getMasterDashboard());
    }
}
