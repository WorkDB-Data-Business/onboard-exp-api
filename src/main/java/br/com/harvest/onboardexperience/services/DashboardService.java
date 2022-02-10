package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.*;
import br.com.harvest.onboardexperience.domain.entities.*;
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
import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;
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

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;

    @Autowired
    private StageService stageService;

    private final String CURRENT_PLAN = "Trial";
    private final BigInteger MAX_REGISTRATIONS_IN_THE_PLAN = BigInteger.TEN;

    public DashboardMasterMetricsDTO getMasterDashboard() throws ApiException {
        return DashboardMasterMetricsDTO.builder()
                .scormCloudMetrics(getScormCloudMetrics())
                .harvestLibraryRanking(getHarvestLibraryRanking(clientService.getHarvestClient()))
                .build();
    }

    public DashboardAdminMetricsDTO getAdminDashboard(@NonNull String token){
        return DashboardAdminMetricsDTO.builder()
                .trailMetrics(getTrailMetrics(token))
                .harvestLibraryRanking(getHarvestLibraryRanking(tenantService.fetchClientByTenantFromToken(token)))
                .build();
    }

    public DashboardColaboratorMetricsDTO getColaboratorDashboard(@NonNull String token){
        return DashboardColaboratorMetricsDTO
                .builder()
                .userTrailMetrics(getUserTrailMetrics(token))
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

    private List<HarvestLibraryRanking> getHarvestLibraryRanking(@NonNull Client client){
        List<HarvestLibraryRanking> harvestLibraryRankings = new ArrayList<>();

        List<Link> links = linkStorageService.findAllByClient(client);
        List<HarvestFile> harvestFiles = harvestFileStorageService.findAllByClient(client).stream().filter(harvestFile -> !harvestFile.getIsAsset()).collect(Collectors.toList());
        List<Scorm> scorms = scormStorageService.findAllByClient(client);

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

    private List<TrailMetrics> getTrailMetrics(@NonNull String token){
        return trailService.findAllByClient(tenantService.fetchClientByTenantFromToken(token)).stream().map(this::getTrailMetric).collect(Collectors.toList());
    }

    private TrailMetrics getTrailMetric(@NonNull Trail trail){
        List<UserMetrics> userMetrics = getUserMetrics(trail);

        List<UserTrailRegistration> registrations = trail.getTrailRegistrations();

        BigInteger activeUsersQuantity = BigInteger.valueOf(registrations.stream().filter(
                userTrailRegistration ->
                        Objects.nonNull(userTrailRegistration.getStartedTrailDate())
                                && Objects.isNull(userTrailRegistration.getFinishedTrailDate())
        ).count());

        BigInteger finishedUsersQuantity = BigInteger.valueOf(registrations.stream().filter(
                userTrailRegistration ->
                        Objects.nonNull(userTrailRegistration.getStartedTrailDate())
                                && Objects.nonNull(userTrailRegistration.getFinishedTrailDate())
        ).count());

        BigDecimal averageScore = userMetrics.stream().map(UserMetrics::getAverageScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TrailMetrics.builder()
                .trailId(trail.getId())
                .name(trail.getName())
                .quantityActiveUsers(activeUsersQuantity)
                .userMetrics(userMetrics)
                .averageScore(averageScore
                        .divide(new BigDecimal(finishedUsersQuantity.compareTo(BigInteger.ZERO) == 0
                                        ? BigInteger.ONE : finishedUsersQuantity)
                                , 2, RoundingMode.HALF_UP))
                .build();
    }

    private List<UserMetrics> getUserMetrics(@NonNull Trail trail){
        return trail.getTrailRegistrations().stream().filter(registration ->
                Objects.nonNull(registration.getFinishedTrailDate())).map(this::getUserMetric).collect(Collectors.toList());
    }

    private UserMetrics getUserMetric(@NonNull UserTrailRegistration registration){
        return UserMetrics
                .builder()
                .name(Optional.ofNullable(registration.getUser().getNickname())
                        .orElse(MessageFormat.format("{0} {1}",
                                registration.getUser().getFirstName(), registration.getUser().getLastName())))
                .id(registration.getUser().getId())
                .averageScore(registration.getAverageScore())
                .averageLengthOfStayOnTrail(getAverageLengthOfStayOnTrail(registration))
                .build();
    }

    private AverageLengthOfStayOnTrail getAverageLengthOfStayOnTrail(@NonNull UserTrailRegistration registration){
        Duration duration = Duration.between(registration.getStartedTrailDate(), registration.getFinishedTrailDate());
        return AverageLengthOfStayOnTrail
                .builder()
                .days(duration.toDaysPart())
                .hours(duration.toHoursPart())
                .minutes(duration.toMinutesPart())
                .build();
    }

    private List<UserTrailMetrics> getUserTrailMetrics(@NonNull String token){
        return trailService.findAllMyTrails(token).stream().map(trail -> getUserTrailMetric(userService.findUserByToken(token),
                trail)).collect(Collectors.toList());
    }

    private UserTrailMetrics getUserTrailMetric(@NonNull User user, @NonNull Trail trail){
        List<StageUser> stageUsers = stageService.getAllStagesFromUserInTrail(user, trail);

        BigDecimal completedStages = BigDecimal.valueOf(stageUsers.stream().filter(StageUser::getIsCompleted).count());
        BigDecimal stagesTrail = BigDecimal.valueOf(trail.getStages().size());

        return UserTrailMetrics.builder()
                .finalizationPercentage(completedStages.divide(stagesTrail, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")))
                .id(trail.getId())
                .name(trail.getName())
                .build();
    }

}
