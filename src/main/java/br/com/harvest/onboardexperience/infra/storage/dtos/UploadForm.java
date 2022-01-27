package br.com.harvest.onboardexperience.infra.storage.dtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class UploadForm {

    public UploadForm(MultipartFile file, LinkForm link, List<Long> authorizedClients, String description) {
        if(Objects.isNull(file)){
            this.link = link;
        } else {
            this.file = file;
        }
        this.authorizedClients = authorizedClients;
        this.description = description;
    }

    private MultipartFile file;

    private LinkForm link;

    private List<Long> authorizedClients;

    private String description;


}
