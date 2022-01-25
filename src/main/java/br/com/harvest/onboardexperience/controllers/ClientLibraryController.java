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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

@Tag(name = "Client Library")
@RestController
@RequestMapping("/v1/client-library")
@CrossOrigin(origins = "*", maxAge = 36000)
public class ClientLibraryController {

    @Autowired
    private StorageAdapter storageAdapter;

    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Faz upload de arquivos e links na biblioteca do Cliente.")
    @PostMapping
    public void upload(@ModelAttribute LinkForm dto,
                       @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestHeader("Authorization") String token) {
        storageAdapter.setFormClient(dto, file,authorizedClients, token).save();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Faz upload de arquivos  e link na biblioteca do CLiente pelo ID.")
    @PutMapping(value="/{id}")
    public void upload(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                       @ModelAttribute LinkForm dto,
                       @RequestParam(value = "authorizedClients", required = false) List<Long> authorizedClients,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestHeader("Authorization") String token)
            throws Exception {
        storageAdapter.setFormClient(dto, file, authorizedClients, token).update(id);
    }

    @Operation(description = "Realiza exclusao da biblioteca do cliente.")
    @DeleteMapping("/{id}/{type}")
    public void delete(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                       @PathVariable Storage type,
                       @RequestHeader("Authorization") String token) throws Exception {
        storageAdapter.setStorageClient(type).delete(id, token);
    }

    @GetMapping("/{type}")
    public ResponseEntity<Page<?>> findAll(Pageable pageable,
                                           @PathVariable  Storage type,
                                           @RequestHeader("Authorization") String token) {
        return findAllByClientAndType(type, token, pageable);
    }
    private ResponseEntity<Page<?>> findAllByClientAndType(@NonNull Storage type, @NonNull String token,
                                                           Pageable pageable){
        switch (type) {
            case CLIENT_FILE: {
                Page<?> files = this.storageAdapter.setStorageClient(Storage.CLIENT_FILE).findAll(token, pageable);

                return ResponseEntity.ok().body(files);
            }
            case LINK: {
                Page<?> link = this.storageAdapter.setStorageClient(Storage.LINK).findAll(token, pageable);
                return ResponseEntity.ok().body(link);
            }
            default: {
                return null;
            }
        }
    }

    @GetMapping("/{id}/{type}")
    public ResponseEntity<?> find(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                  @PathVariable  Storage type,
                                  @RequestHeader("Authorization") String token) throws Exception {

        return findByIdAndType(id, type, token);
    }
    private ResponseEntity<?> findByIdAndType(@NonNull Long id, @NonNull Storage type, @NonNull String token) throws Exception {
        switch (type) {
            case CLIENT_FILE: {
                Optional<?> file = this.storageAdapter.setStorageClient(Storage.CLIENT_FILE).find(id, token);

                Object fileName = GenericUtils.executeMethodFromGenericClass(FileDto.class, "getName", file);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(file.get());
            }
            case LINK: {
                Optional<?> link = this.storageAdapter.setStorageClient(Storage.LINK).find(id, token);
                return ResponseEntity.ok().body(link.get());
            }
            default: {
                return null;
            }
        }
    }

    @PatchMapping(value="/authorize/{id}/{type}")
    public void updateAuthorizedClients(@PathVariable  @Pattern(regexp = RegexUtils.ONLY_NUMBERS) Long id,
                                        @PathVariable  Storage type,
                                        @RequestParam(value = "authorizedClients") List<Long> authorizedClients,
                                        @RequestHeader("Authorization") String token)
            throws Exception {
        storageAdapter.setStorage(type).updateAuthorizedClients(id, token, authorizedClients);
    }


}
