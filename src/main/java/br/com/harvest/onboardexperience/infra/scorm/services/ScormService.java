package br.com.harvest.onboardexperience.infra.scorm.services;

import br.com.harvest.onboardexperience.infra.scorm.ScormAPI;
import br.com.harvest.onboardexperience.infra.scorm.dtos.ScormDto;
import br.com.harvest.onboardexperience.infra.scorm.entities.Scorm;
import br.com.harvest.onboardexperience.infra.scorm.mappers.ScormMapper;
import br.com.harvest.onboardexperience.infra.scorm.repository.ScormRepository;
import br.com.harvest.onboardexperience.services.TenantService;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import com.rusticisoftware.cloud.v2.client.ApiException;
import com.rusticisoftware.cloud.v2.client.model.CourseSchema;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class ScormService {

    @Autowired
    private ScormAPI scormAPI;

    @Autowired
    private ScormRepository repository;

    @Autowired
    private TenantService tenantService;

    public ScormDto importScormCourse(@NonNull MultipartFile file, @NonNull String token) throws IOException, ApiException {
        return ScormMapper.INSTANCE.toDto(save(createScorm(uploadScormToScormCloud(file), token)));
    }

    private CourseSchema uploadScormToScormCloud(@NonNull MultipartFile file) throws IOException, ApiException {
        String courseID = GenericUtils.generateUUID();
        return scormAPI.createCourse(courseID, createTempFile(file, courseID));
    }

    private Scorm save(@NonNull Scorm scorm){
        return repository.save(scorm);
    }

    private File createTempFile(MultipartFile file, String courseID) throws IOException {
        File tempfile = File.createTempFile(courseID + "-", file.getOriginalFilename());
        file.transferTo(tempfile);
        tempfile.deleteOnExit();
        return tempfile;
    }

    private Scorm createScorm(CourseSchema schema, String token){
        return Scorm.builder()
                .id(schema.getId())
                .title(schema.getTitle())
                .client(tenantService.fetchClientByTenantFromToken(token))
                .build();
    }

}
