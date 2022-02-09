package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.DashboardMasterMetricsDTO;
import br.com.harvest.onboardexperience.domain.dtos.ScormCloudMetricsDTO;
import br.com.harvest.onboardexperience.infra.scorm.ScormAPI;
import br.com.harvest.onboardexperience.infra.scorm.filters.ScormRegistrationFilter;
import com.rusticisoftware.cloud.v2.client.ApiException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Service
public class DashboardService {

    @Autowired
    private ScormAPI scormAPI;

    private final String CURRENT_PLAN = "Trial";
    private final BigInteger MAX_REGISTRATIONS_IN_THE_PLAN = BigInteger.TEN;

    public DashboardMasterMetricsDTO getMasterDashboard() throws ApiException {
        return DashboardMasterMetricsDTO.builder()
                .scormCloudMetrics(getScormCloudMetrics())
                .build();
    }

    private ScormCloudMetricsDTO getScormCloudMetrics() throws ApiException {

        BigInteger activeRegistrations = BigInteger.valueOf(scormAPI.getAllRegistrations(new ScormRegistrationFilter()).size());

        return ScormCloudMetricsDTO
                .builder()
                .currentPlan(CURRENT_PLAN)
                .quantityScormRegistrationsCurrentlyActive(activeRegistrations)
                .maxRegistrationsInThePlan(MAX_REGISTRATIONS_IN_THE_PLAN)
                .percentageUsageRegistrationPlan(getPercentageUsagePlan(activeRegistrations))
                .build();
    }

    private BigDecimal getPercentageUsagePlan(@NonNull BigInteger activeRegistrations){
        return new BigDecimal(activeRegistrations).divide(new BigDecimal(MAX_REGISTRATIONS_IN_THE_PLAN), 2,
                RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }
}
