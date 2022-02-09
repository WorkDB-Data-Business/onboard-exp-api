package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
import br.com.harvest.onboardexperience.domain.exceptions.GenericUploadException;
import br.com.harvest.onboardexperience.domain.exceptions.LinkNotFoundException;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.LinkSimpleDto;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import br.com.harvest.onboardexperience.infra.storage.enumerators.Storage;
import br.com.harvest.onboardexperience.infra.storage.filters.HarvestLibraryFilter;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.LinkMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.LinkRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LinkStorageService implements StorageService {

    @Autowired
    private LinkRepository repository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private UserService userService;

    @Autowired
    private AssetStorageService assetStorageService;

    private final Function<LinkSimpleDto, LinkSimpleDto> SET_STORAGE = linkSimpleDto -> {
        linkSimpleDto.setStorage(Storage.LINK);
        return linkSimpleDto;
    };

    @Override
    public void save(@NonNull UploadForm form, @NonNull String token) {
        validate(form);

        User author = userService.findUserByToken(token);

        Link link = Link
                .builder()
                .description(form.getDescription())
                .authorizedClients(fetchService.generateAuthorizedClients(form.getAuthorizedClients(), author))
                .author(author)
                .build();

        BeanUtils.copyProperties(form.getLink(), link);

        uploadImage(link, form);

        repository.save(link);
    }

    public void uploadImage(@NonNull Link link, @NonNull UploadForm form){
        link.setPreviewImagePath(
                assetStorageService.uploadAsset(form.getPreviewImage(),
                        link.getAuthor().getClient().getCnpj(),
                        GenericUtils.generateUUID() + "_preview",
                        FileTypeEnum.ASSET, link.getAuthor()));
    }

    @Override
    public void validate(@NonNull UploadForm form) {
        if(Objects.isNull(form.getLink())){
            throw new GenericUploadException("The link cannot be null.");
        }

        if(Objects.isNull(form.getPreviewImage())){
            throw new GenericUploadException("The preview image cannot be null.");
        }
    }

    @Override
    public Page<LinkSimpleDto> findAll(@NonNull String token, HarvestLibraryFilter filter, Pageable pageable) {
        return repository.findAll(createQuery(filter, token), pageable)
                .map(LinkMapper.INSTANCE::toLinkSimpleDto)
                .map(SET_STORAGE);
    }

    public List<Link> findAllByClient(Client client) {
        return repository.findAll(LinkRepository.byClient(client));
    }

    private Specification<Link> createQuery(@NonNull HarvestLibraryFilter filter, @NonNull String token){
        Specification<Link> query = Specification.where(LinkRepository.byAuthorizedClients(tenantService.fetchClientByTenantFromToken(token)));

        if(StringUtils.hasText(filter.getCustomFilter())){
            query = query.and(LinkRepository.byCustomFilter(filter.getCustomFilter()));
        }

        return query;
    }

    @Override
    public void update(@NonNull String id, @NonNull UploadForm form, @NonNull String token) {

        Link link = getLinkByIdAndAuthorizedClient(id, token, true);

        Boolean needToImagePreview = Objects.nonNull(form.getPreviewImage());

        link.setAuthorizedClients(fetchService.generateAuthorizedClients(form.getAuthorizedClients(), link.getAuthor()));

        BeanUtils.copyProperties(form, link, "id", "author", "createdBy", "createdAt",
                !needToImagePreview ? "previewImagePath" : null);

        BeanUtils.copyProperties(form.getLink(), link, "id", "author", "createdBy", "createdAt",
                !needToImagePreview ? "previewImagePath" : null);

        if(needToImagePreview){
            uploadImage(link, form);
        }

        repository.save(link);
    }

    @Override
    public void delete(@NonNull String id, @NonNull String token) {
        Link link = getLinkByIdAndAuthorizedClient(id, token, true);
        repository.delete(link);
    }

    @Override
    public Optional<LinkDto> find(@NonNull String id, @NonNull String token) {

        Link link = getLinkByIdAndAuthorizedClient(id, token, false);

        LinkDto dto = LinkMapper.INSTANCE.toDto(link);

        dto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(link.getAuthorizedClients(), Client.class));
        dto.setStorage(Storage.LINK);

        return Optional.of(dto);
    }

    @Override
    public void updateAuthorizedClients(@NonNull String id, @NonNull String token, @NonNull List<Long> authorizedClients) {
        Link link = getLinkByIdAndAuthorizedClient(id, token, true);

        link.setAuthorizedClients(fetchService.fetchClients(authorizedClients));

        repository.save(link);
    }

    public Link getLinkByIdAndAuthorizedClient(@NonNull String id, @NonNull String token, Boolean validateAuthor){
        User user = userService.findUserByToken(token);

        Link link = repository
                .findOne(LinkRepository.byIdAsString(id).and(LinkRepository.byAuthorizedClients(user.getClient())))
                .or(() -> repository.findOne(LinkRepository.byIdAsString(id).and(LinkRepository.byAuthor(user))))
                .orElseThrow(
                () -> new LinkNotFoundException("Link not found.", "The requested link doesn't exist or you don't have access to get it.")
        );

        if(validateAuthor){
            StorageService.validateAuthor(link, Link.class, user, "You're not the author of the link.");
        }

        return link;
    }
}
