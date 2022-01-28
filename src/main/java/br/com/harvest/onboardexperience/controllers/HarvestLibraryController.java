package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkForm;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.filters.HarvestLibraryFilter;
import br.com.harvest.onboardexperience.infra.storage.services.StorageAdapter;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.RegexUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

@Tag(name = "Harvest Library")
@RestController
@RequestMapping("/v1/harvest-library")
@CrossOrigin(origins = "*", maxAge = 36000)
public class HarvestLibraryController {

    @Autowired
    private StorageAdapter storageAdapter;

    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Faz upload de arquivos e links na biblioteca da Harvest.")
    @PostMapping(value = "/{id}/{type}")
    public void upload(@PathVariable Storage type,
                       @ModelAttribute LinkForm dto,
                       @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
                       @RequestParam(value = "description", required = false) String description,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestParam(value = "previewImage") MultipartFile previewImage,
                       @RequestHeader("Authorization") String token) {
        storageAdapter.setForm(dto, file, previewImage, authorizedClients, description, name, type, token).save();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value="/{id}/{type}")
    public void upload(@PathVariable Storage type,
                       @PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                       @ModelAttribute LinkForm dto,
                       @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
                       @RequestParam(value = "description", required = false) String description,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestParam(value = "previewImage") MultipartFile previewImage,
                       @RequestHeader("Authorization") String token)
            throws Exception {
        storageAdapter.setForm(dto, file, previewImage, authorizedClients, description, name, type, token).update(id);
    }

    @DeleteMapping("/{id}/{type}")
    public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                       @PathVariable Storage type,
                       @RequestHeader("Authorization") String token) throws Exception {
        storageAdapter.setStorage(type).delete(id, token);
    }

    @GetMapping("/{id}/{type}")
    public ResponseEntity<?> find(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                  @PathVariable  Storage type,
                                  @RequestHeader("Authorization") String token) throws Exception {

        return findByIdAndType(id, type, token);
    }

    @GetMapping("/{type}")
    public ResponseEntity<Page<?>> findAll(Pageable pageable,
                                           @PathVariable  Storage type,
                                           @Valid HarvestLibraryFilter filter,
                                           @RequestHeader("Authorization") String token) {
        return findAllByClientAndType(type, filter, token, pageable);
    }

    @PatchMapping(value="/authorize/{id}/{type}")
    public void updateAuthorizedClients(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                        @PathVariable  Storage type,
                       @RequestParam(value = "authorizedClients") List<Long> authorizedClients,
                       @RequestHeader("Authorization") String token)
            throws Exception {
        storageAdapter.setStorage(type).updateAuthorizedClients(id, token, authorizedClients);
    }


    private ResponseEntity<Page<?>> findAllByClientAndType(@NonNull Storage type, @NonNull HarvestLibraryFilter filter, @NonNull String token,
                                                           Pageable pageable){
        switch (type) {
            case HARVEST_FILE: {
                Page<?> files = this.storageAdapter.setStorage(Storage.HARVEST_FILE).findAll(token, filter, pageable);
                return ResponseEntity.ok().body(files);
            }
            case LINK: {
                Page<?> link = this.storageAdapter.setStorage(Storage.LINK).findAll(token, filter, pageable);
                return ResponseEntity.ok().body(link);
            }
            case SCORM: {
                Page<?> courses = this.storageAdapter.setStorage(Storage.SCORM).findAll(token, filter, pageable);
                return ResponseEntity.ok().body(courses);
            }
            default: {
                return null;
            }
        }
    }

    private ResponseEntity<?> findByIdAndType(@NonNull Long id, @NonNull Storage type, @NonNull String token) throws Exception {
        switch (type) {
            case HARVEST_FILE: {
                Optional<?> file = this.storageAdapter.setStorage(Storage.HARVEST_FILE).find(id, token);

                Object fileName = GenericUtils.executeMethodFromGenericClass(FileDto.class, "getName", file);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(file.orElse(null));
            }
            case LINK: {
                Optional<?> link = this.storageAdapter.setStorage(Storage.LINK).find(id, token);
                return ResponseEntity.ok().body(link.orElse(null));
            }
            case SCORM: {
                Optional<?> course = this.storageAdapter.setStorage(Storage.SCORM).find(id, token);
                return ResponseEntity.ok().body(course.orElse(null));
            }
            default: {
                return null;
            }
        }
    }
}
