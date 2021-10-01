package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.infra.storage.dtos.FileDto;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.FileMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileContentStore;
import br.com.harvest.onboardexperience.infra.storage.repositories.FileRepository;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Optional;

@Service
public class FileStorageService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileContentStore contentStore;

    public Optional<FileDto> find(String contentPath) throws FileNotFoundException {
        if(GenericUtils.stringNullOrEmpty(contentPath)){
            throw new NullPointerException("The content path can't be null.");
        }

        HarvestFile file = fileRepository.findByContentPath(contentPath).orElseThrow(
                () -> new FileNotFoundException("File not found.")
        );

        FileDto dto = FileMapper.INSTANCE.toDto(file);

        dto.setFileEncoded(StorageService.encodeFileToBase64(contentStore.getContent(file)));

        return Optional.of(dto);
    }
}
