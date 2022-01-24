package br.com.harvest.onboardexperience.infra.scorm.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.exceptions.RegistrationNotFoundException;
import br.com.harvest.onboardexperience.domain.exceptions.ScormCourseNotFoundException;
import br.com.harvest.onboardexperience.infra.scorm.ScormAPI;
import br.com.harvest.onboardexperience.infra.scorm.dtos.ScormDto;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.infra.scorm.entities.ScormRegistration;
import br.com.harvest.onboardexperience.infra.scorm.factories.ScormRegistrationFactory;
import br.com.harvest.onboardexperience.infra.scorm.filters.ScormCourseFilter;
import br.com.harvest.onboardexperience.infra.scorm.mappers.ScormMapper;
import br.com.harvest.onboardexperience.infra.scorm.repository.ScormRegistrationRepository;
import br.com.harvest.onboardexperience.infra.scorm.repository.ScormRepository;
import br.com.harvest.onboardexperience.services.TenantService;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import com.rusticisoftware.cloud.v2.client.ApiException;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import com.rusticisoftware.cloud.v2.client.model.CreateRegistrationSchema;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ScormService {

    @Autowired
    private ScormAPI scormAPI;

    @Autowired
    private ScormRepository courseRepository;

    @Autowired
    private ScormRegistrationRepository registrationRepository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    public Page<ScormDto> findAll(Pageable pageable, @NonNull ScormCourseFilter filter, @NonNull String token){
        return courseRepository
                .findAll(buildFilterParams(filter, token), buildCustomPageable(pageable, filter))
                .map(ScormMapper.INSTANCE::toDto);
    }

    private Specification<Scorm> buildFilterParams(@NonNull ScormCourseFilter filter, @NonNull String token){
        Specification<Scorm> filterParams = Specification.where(
                ScormRepository.byClient(tenantService.fetchClientByTenantFromToken(token))
        );

        if(ObjectUtils.isNotEmpty(filter.getSince()) || ObjectUtils.isNotEmpty(filter.getUntil())){
            filterParams.and(ScormRepository.betweenSinceAndUntil(filter.getSince().toLocalDateTime(),
                    filter.getUntil().toLocalDateTime()));
        }
        return filterParams;
    }

    private Pageable buildCustomPageable(Pageable pageable, @NonNull ScormCourseFilter filter){
        if(ObjectUtils.isNotEmpty(filter.getOrderBy())){
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(filter.getOrderBy()).ascending());
        }
        return pageable;
    }

    public ScormDto importScormCourse(@NonNull MultipartFile file, @NonNull String token) throws IOException, ApiException {
        return ScormMapper.INSTANCE.toDto(save(createScormCourse(uploadScormToScormCloud(file), token)));
    }

    private CourseSchema uploadScormToScormCloud(@NonNull MultipartFile file) throws IOException, ApiException {
        String courseID = GenericUtils.generateUUID();
        return scormAPI.createCourse(courseID, createTempFile(file, courseID));
    }

    public void deleteScormCourse(@NonNull String scormId, @NonNull String token) throws ApiException {
        Scorm scorm = findByIdAndToken(scormId, token);
        scormAPI.deleteCourse(scorm.getId());
        courseRepository.delete(scorm);
    }

    private Scorm save(@NonNull Scorm scorm){
        return courseRepository.save(scorm);
    }

    private File createTempFile(MultipartFile file, String courseID) throws IOException {
        File tempfile = File.createTempFile(courseID + "-", file.getOriginalFilename());
        file.transferTo(tempfile);
        tempfile.deleteOnExit();
        return tempfile;
    }

    public void registerOnScormCourse(@NonNull String scormID, @NonNull String token) throws ApiException, ScormCourseNotFoundException {
        Scorm scorm = findByIdAndToken(scormID, token);
        ScormRegistration registration = createScormCourseRegistration(scorm, token);
        uploadRegistrationToScormCloud(registration);
        registrationRepository.save(registration);
    }

    public ScormRegistration findScormRegistrationByIdAndToken(@NonNull String courseId, @NonNull String registrationId, @NonNull String token){
        User user = userService.findUserById(tokenUtils.getUserId(token));
        Scorm scorm = findByIdAndToken(courseId, token);

        return registrationRepository.findByUserAndScormAndIsActive(user, scorm, true).orElseThrow(
                () -> new RegistrationNotFoundException("The user has no register in this course.")
        );
    }

    public String generateScormExecutionLink(@NonNull String courseId, @NonNull String registrationId, @NonNull String token) throws ApiException {
        return scormAPI.buildLaunchLink(findScormRegistrationByIdAndToken(courseId, registrationId, token).getId());
    }

    public void deleteRegistration(@NonNull String scormId, @NonNull Long userId) throws ApiException {
        User user = userService.findUserById(userId);
        Scorm scorm = findByIdAndClient(scormId, user.getClient());
        Optional<ScormRegistration> registration = registrationRepository.findByUserAndScormAndIsActive(user, scorm, true);
        if(registration.isPresent()){
            scormAPI.deleteRegistration(registration.get().getId());
            registration.map(this::disableRegistration).ifPresent(registrationRepository::save);
        }
    }

    public void deleteRegistration(@NonNull String scormId, @NonNull String token) throws ApiException {
        deleteRegistration(scormId, userService.findUserById(tokenUtils.getUserId(token)).getId());
    }

    private ScormRegistration disableRegistration(ScormRegistration registration){
        registration.setIsActive(false);
        return registration;
    }

    public Scorm findByIdAndToken(@NonNull String scormID, @NonNull String token) throws ScormCourseNotFoundException {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return findByIdAndClient(scormID, client);
    }

    public Scorm findByIdAndClient(@NonNull String scormID, @NonNull Client client) throws ScormCourseNotFoundException {
        return courseRepository.findByIdAndClient(scormID, client).orElseThrow(() -> new ScormCourseNotFoundException("ID", scormID));
    }

    private Scorm createScormCourse(@NonNull CourseSchema schema, @NonNull String token){
        Scorm scorm = ScormMapper.INSTANCE.fromCourseSchemaToEntity(schema);
        setClientOnScormCourse(scorm, token);
        return scorm;
    }

    private void setClientOnScormCourse(@NonNull Scorm scorm, @NonNull String token){
        scorm.setClient(tenantService.fetchClientByTenantFromToken(token));
    }

    private void uploadRegistrationToScormCloud(@NonNull ScormRegistration scormRegistration) throws ApiException {
        CreateRegistrationSchema schema = createRegistrationSchema(scormRegistration);
        scormAPI.createRegistration(schema);
    }

    private CreateRegistrationSchema createRegistrationSchema(@NonNull ScormRegistration scormRegistration){
        return ScormRegistrationFactory.createRegistrationSchema(scormRegistration.getScorm(), scormRegistration.getUser(),
                scormRegistration.getId());
    }

    private ScormRegistration createScormCourseRegistration(@NonNull Scorm course, @NonNull String token){
        User user = userService.findUserById(tokenUtils.getUserId(token));

        return ScormRegistration.builder()
                .id(GenericUtils.generateUUID())
                .scorm(course)
                .user(user)
                .build();
    }

}
