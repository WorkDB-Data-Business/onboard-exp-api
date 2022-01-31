package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.infra.storage.entities.Image;
import br.com.harvest.onboardexperience.infra.storage.repositories.ImageContentStore;
import br.com.harvest.onboardexperience.infra.storage.repositories.ImageRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;

@Slf4j
@Service
public class ImageStorageService {

    @Autowired
    private ImageContentStore contentStore;

    @Autowired
    private ImageRepository repository;

    private final String FILE_FOLDER = "files";

    public String uploadImage(@NonNull MultipartFile file, @NonNull String folder, String fileNameParam, @NonNull FileTypeEnum imageDomain, User author){
        String fileName = MessageFormat.format("{0}-{1}.{2}", fileNameParam, GenericUtils.generateUUID(), FilenameUtils.getExtension(file.getOriginalFilename()));

        Image image = Image.builder()
                .contentPath(createFilePath(folder, fileName, imageDomain))
                .fileName(fileName)
                .mimeType(file.getContentType())
                .author(author)
                .build();

        uploadImage(image, file);

        repository.save(image);
        return image.getContentPath();
    }

    private void uploadImage(@NonNull Image image, @NonNull MultipartFile file) {
        try {
            contentStore.setContent(image, file.getInputStream());
        } catch (IOException e) {
            log.error("An error occurs while uploading the file", e);
            throw new GenericUploadException(e.getMessage(), e.getCause());
        }
    }

    private String createFilePath(@NonNull String folder, String fileName, @NonNull FileTypeEnum imageDomain) {
        StringBuilder builder = new StringBuilder(FILE_FOLDER)
                .append("/")
                .append(folder)
                .append("/")
                .append(imageDomain.getName())
                .append("/")
                .append(fileName);
        return builder.toString();
    }

}
