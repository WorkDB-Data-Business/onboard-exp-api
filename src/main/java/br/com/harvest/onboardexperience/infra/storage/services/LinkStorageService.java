package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.domain.exceptions.LinkNotFoundException;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.LinkMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.LinkRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
public class LinkStorageService implements StorageService {

    @Autowired
    private LinkRepository repository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private FetchService fetchService;

    private final Function<LinkSimpleDto, LinkSimpleDto> SET_STORAGE = linkSimpleDto -> {
        linkSimpleDto.setStorage(Storage.LINK);
        return linkSimpleDto;
    };

    @Override
    public void save(@NonNull UploadForm form, @NonNull String token) {
        validate(form);

        Link link = Link
                .builder()
                .authorizedClients(fetchService.fetchClients(form.getAuthorizedClients()))
                .author(tenantService.fetchClientByTenantFromToken(token))
                .build();

        BeanUtils.copyProperties(form.getLink(), link);

        repository.save(link);
    }

    @Override
    public void validate(@NonNull UploadForm form) {
        if(Objects.isNull(form.getLink())){
            throw new NullPointerException("The link cannot be null.");
        }
    }

    @Override
    public Page<?> findAll(@NonNull String token, Pageable pageable) {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return repository.findAllByAuthorizedClients(client, pageable)
                .map(LinkMapper.INSTANCE::toLinkSimpleDto)
                .map(SET_STORAGE);
    }

    @Override
    public void update(@NonNull Long id, @NonNull UploadForm form, @NonNull String token) {

        Link link = getLinkByIdAndAuthorizedClient(id, token);

        link.setAuthorizedClients(fetchService.fetchClients(form.getAuthorizedClients()));

        BeanUtils.copyProperties(form.getLink(), link, "id", "author", "createdBy", "createdAt");

        repository.save(link);
    }

    @Override
    public void delete(@NonNull Long id, @NonNull String token) {
        Link link = getLinkByIdAndAuthorizedClient(id, token);
        repository.delete(link);
    }

    @Override
    public Optional<LinkDto> find(@NonNull Long id, @NonNull String token) {

        Link link = getLinkByIdAndAuthorizedClient(id, token);

        LinkDto dto = LinkMapper.INSTANCE.toDto(link);

        dto.setAuthorizedClientsId(StorageService.getIDFromClients(link.getAuthorizedClients()));
        dto.setStorage(Storage.LINK);

        return Optional.of(dto);
    }

    @Override
    public void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull List<Long> authorizedClients) {
        Link link = getLinkByIdAndAuthorizedClient(id, token);

        link.setAuthorizedClients(fetchService.fetchClients(authorizedClients));

        repository.save(link);
    }

    private Link getLinkByIdAndAuthorizedClient(@NonNull Long id, @NonNull String token){
        Client client = tenantService.fetchClientByTenantFromToken(token);

        Link link = repository.findByIdAndAuthorizedClientsOrAuthor(id, client, client).orElseThrow(
                () -> new LinkNotFoundException("Link not found.", "The requested link doesn't exist or you don't have access to get it.")
        );

        return link;
    }
}
