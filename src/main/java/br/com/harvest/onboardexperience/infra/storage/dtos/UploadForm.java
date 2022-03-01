package br.com.harvest.onboardexperience.infra.storage.dtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class UploadForm {

    public UploadForm(MultipartFile file, MultipartFile previewImage, LinkForm link, List<Long> authorizedClients, String description, String name) {
        if(Objects.isNull(file)){
            this.link = link;
        } else {
            this.file = file;
        }
        this.authorizedClients = authorizedClients;
        this.description = description;
        this.name = name;
        this.previewImage = previewImage;
    }

    private MultipartFile file;

    private MultipartFile previewImage;

    private LinkForm link;

    private List<Long> authorizedClients;

    private String description;

    private String name;
    
}
