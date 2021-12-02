package br.com.harvest.onboardexperience.controllers;

import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkForm;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
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
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;
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
    @PostMapping
    public void upload(@ModelAttribute LinkForm dto,
                       @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestHeader("Authorization") String token) {
        storageAdapter.setForm(dto, file, authorizedClients, token).save();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value="/{id}")
    public void upload(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                       @ModelAttribute LinkForm dto,
                       @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestHeader("Authorization") String token)
            throws Exception {
        storageAdapter.setForm(dto, file, authorizedClients, token).update(id);
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
                                           @RequestHeader("Authorization") String token) {
        return findAllByClientAndType(type, token, pageable);
    }

    @PatchMapping(value="/authorize/{id}/{type}")
    public void updateAuthorizedClients(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                        @PathVariable  Storage type,
                       @RequestParam(value = "authorizedClients") List<Long> authorizedClients,
                       @RequestHeader("Authorization") String token)
            throws Exception {
        storageAdapter.setStorage(type).updateAuthorizedClients(id, token, authorizedClients);
    }


    private ResponseEntity<Page<?>> findAllByClientAndType(@NonNull Storage type, @NonNull String token,
                                                     Pageable pageable){
        switch (type) {
            case HARVEST_FILE: {
                Page<?> files = this.storageAdapter.setStorage(Storage.HARVEST_FILE).findAll(token, pageable);

                return ResponseEntity.ok().body(files);
            }
            case LINK: {
                Page<?> link = this.storageAdapter.setStorage(Storage.LINK).findAll(token, pageable);
                return ResponseEntity.ok().body(link);
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
                        .body(file.get());
            }
            case LINK: {
                Optional<?> link = this.storageAdapter.setStorage(Storage.LINK).find(id, token);
                return ResponseEntity.ok().body(link.get());
            }
            default: {
                return null;
            }
        }
    }
}
