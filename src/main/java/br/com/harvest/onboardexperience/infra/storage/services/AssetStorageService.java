package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.infra.storage.entities.Asset;
import br.com.harvest.onboardexperience.infra.storage.repositories.AssetContentStore;
import br.com.harvest.onboardexperience.infra.storage.repositories.AssetRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Objects;

@Slf4j
@Service
public class AssetStorageService {

    @Autowired
    private AssetContentStore contentStore;

    @Autowired
    private AssetRepository repository;

    private final String FILE_FOLDER = "files";

    public String uploadAsset(@NonNull MultipartFile file, @NonNull String folder, String fileNameParam, @NonNull FileTypeEnum imageDomain, User author) {
        String fileName = MessageFormat.format("{0}-{1}.{2}", fileNameParam, GenericUtils.generateUUID(), FilenameUtils.getExtension(file.getOriginalFilename()));

        Asset asset = Asset.builder()
                .contentPath(createFilePath(folder, fileName, imageDomain))
                .fileName(fileName)
                .mimeType(file.getContentType())
                .author(author)
                .build();

        uploadAsset(asset, file, imageDomain);

        repository.save(asset);
        return asset.getContentPath();
    }

    private InputStream generateThumbnail(MultipartFile file) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        ByteArrayInputStream input = null;
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(Thumbnails.of(file.getInputStream()).scale(0.45).asBufferedImage(),
                    Objects.isNull(fileExtension) ? "jpeg" : fileExtension, os);
            input = new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            log.info("An error occurred while generating the thumbnail of file", e);
        }
        return input;
    }

    private void uploadAsset(@NonNull Asset asset, @NonNull MultipartFile file, FileTypeEnum fileTypeEnum) {
        try {
            contentStore.setContent(asset, fileTypeEnum.equals(FileTypeEnum.THUMBNAIL) ? generateThumbnail(file) : file.getInputStream());
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
