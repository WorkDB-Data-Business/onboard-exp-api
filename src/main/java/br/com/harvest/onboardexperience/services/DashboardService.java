package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.DashboardMasterMetricsDTO;
import br.com.harvest.onboardexperience.domain.dtos.HarvestLibraryRanking;
import br.com.harvest.onboardexperience.domain.dtos.ScormCloudMetricsDTO;
import br.com.harvest.onboardexperience.domain.entities.Trail;
import br.com.harvest.onboardexperience.infra.scorm.ScormAPI;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.infra.scorm.filters.ScormRegistrationFilter;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.services.HarvestFileStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.LinkStorageService;
import br.com.harvest.onboardexperience.infra.storage.services.ScormStorageService;
import br.com.harvest.onboardexperience.repositories.HarvestFileMediaStageRepository;
import br.com.harvest.onboardexperience.repositories.LinkMediaStageRepository;
import br.com.harvest.onboardexperience.repositories.ScormMediaStageRepository;
import com.rusticisoftware.cloud.v2.client.ApiException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ScormAPI scormAPI;

    @Autowired
    private LinkMediaStageRepository linkMediaStageRepository;

    @Autowired
    private HarvestFileMediaStageRepository harvestFileMediaStageRepository;

    @Autowired
    private ScormMediaStageRepository scormMediaStageRepository;

    @Autowired
    private ScormStorageService scormStorageService;

    @Autowired
    private HarvestFileStorageService harvestFileStorageService;

    @Autowired
    private LinkStorageService linkStorageService;

    @Autowired
    private TrailService trailService;

    @Autowired
    private ClientService clientService;

    private final String CURRENT_PLAN = "Trial";
    private final BigInteger MAX_REGISTRATIONS_IN_THE_PLAN = BigInteger.TEN;

    public DashboardMasterMetricsDTO getMasterDashboard() throws ApiException {
        return DashboardMasterMetricsDTO.builder()
                .scormCloudMetrics(getScormCloudMetrics())
                .harvestLibraryRanking(getHarvestLibraryRanking())
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

    private List<HarvestLibraryRanking> getHarvestLibraryRanking(){
        List<HarvestLibraryRanking> harvestLibraryRankings = new ArrayList<>();

        List<Link> links = linkStorageService.findAllByClient(clientService.getHarvestClient());
        List<HarvestFile> harvestFiles = harvestFileStorageService.findAllByClient(clientService.getHarvestClient()).stream().filter(harvestFile -> !harvestFile.getIsAsset()).collect(Collectors.toList());
        List<Scorm> scorms = scormStorageService.findAllByClient(clientService.getHarvestClient());

        harvestLibraryRankings.addAll(links.stream().map(this::getHarvestLibraryRankingForLink).collect(Collectors.toList()));
        harvestLibraryRankings.addAll(harvestFiles.stream().map(this::getHarvestLibraryRankingForHarvestFile).collect(Collectors.toList()));
        harvestLibraryRankings.addAll(scorms.stream().map(this::getHarvestLibraryRankingForScorm).collect(Collectors.toList()));

        return harvestLibraryRankings.stream()
                .sorted(Comparator.comparing(HarvestLibraryRanking::getUsageQuantity)
                .reversed()).limit(5).collect(Collectors.toList());
    }

    private HarvestLibraryRanking getHarvestLibraryRankingForLink(@NonNull Link link){
        return HarvestLibraryRanking
                .builder()
                .id(link.getId().toString())
                .name(link.getLink())
                .usageQuantity(BigInteger.valueOf(linkMediaStageRepository.countByLink(link)))
                .storage(Storage.LINK)
                .build();
    }

    private HarvestLibraryRanking getHarvestLibraryRankingForHarvestFile(@NonNull HarvestFile harvestFile){
        return HarvestLibraryRanking
                .builder()
                .id(harvestFile.getId().toString())
                .name(harvestFile.getName())
                .usageQuantity(BigInteger.valueOf(harvestFileMediaStageRepository.countByHarvestFile(harvestFile)))
                .storage(Storage.HARVEST_FILE)
                .build();
    }

    private HarvestLibraryRanking getHarvestLibraryRankingForScorm(@NonNull Scorm scorm){
        return HarvestLibraryRanking
                .builder()
                .id(scorm.getId())
                .name(scorm.getTitle())
                .usageQuantity(BigInteger.valueOf(scormMediaStageRepository.countByScorm(scorm)))
                .storage(Storage.SCORM)
                .build();
    }

}
