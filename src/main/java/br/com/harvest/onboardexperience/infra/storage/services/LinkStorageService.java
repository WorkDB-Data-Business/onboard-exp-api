package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.enumerators.FileTypeEnum;
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
import br.com.harvest.onboardexperience.services.UserService;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
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

    @Autowired
    private UserService userService;

    @Autowired
    private ImageStorageService imageStorageService;

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
                .authorizedClients(generateAuthorizedClients(form.getAuthorizedClients(), author))
                .author(author)
                .build();

        BeanUtils.copyProperties(form.getLink(), link);

        uploadImage(link, form);

        repository.save(link);
    }

    public void uploadImage(@NonNull Link link, @NonNull UploadForm form){
        link.setPreviewImagePath(
                imageStorageService.uploadImage(form.getPreviewImage(),
                        link.getAuthor().getClient().getCnpj(),
                        GenericUtils.generateUUID() + "_preview",
                        FileTypeEnum.IMAGE, link.getAuthor()));
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

        Link link = getLinkByIdAndAuthorizedClient(id, token, true);

        link.setAuthorizedClients(generateAuthorizedClients(form.getAuthorizedClients(), link.getAuthor()));

        BeanUtils.copyProperties(form.getLink(), link, "id", "author", "createdBy", "createdAt");

        uploadImage(link, form);

        repository.save(link);
    }

    private List<Client> generateAuthorizedClients(List<Long> clientsId, @NonNull User user){
        if(ObjectUtils.isEmpty(clientsId)){
            clientsId = new ArrayList<>();
        }

        clientsId.add(user.getClient().getId());

        return fetchService.fetchClients(clientsId);
    }

    @Override
    public void delete(@NonNull Long id, @NonNull String token) {
        Link link = getLinkByIdAndAuthorizedClient(id, token, true);
        repository.delete(link);
    }

    @Override
    public Optional<LinkDto> find(@NonNull Long id, @NonNull String token) {

        Link link = getLinkByIdAndAuthorizedClient(id, token, false);

        LinkDto dto = LinkMapper.INSTANCE.toDto(link);

        dto.setAuthorizedClientsId(GenericUtils.extractIDsFromList(link.getAuthorizedClients(), Client.class));
        dto.setStorage(Storage.LINK);

        return Optional.of(dto);
    }

    @Override
    public void updateAuthorizedClients(@NonNull Long id, @NonNull String token, @NonNull List<Long> authorizedClients) {
        Link link = getLinkByIdAndAuthorizedClient(id, token, true);

        link.setAuthorizedClients(fetchService.fetchClients(authorizedClients));

        repository.save(link);
    }

    private Link getLinkByIdAndAuthorizedClient(@NonNull Long id, @NonNull String token, Boolean validateAuthor){
        User user = userService.findUserByToken(token);

        Link link = repository.findByIdAndAuthorizedClients(id, user.getClient()).or(() -> repository.findByIdAndAuthor(id, user))
                .orElseThrow(
                () -> new LinkNotFoundException("Link not found.", "The requested link doesn't exist or you don't have access to get it.")
        );

        if(validateAuthor){
            StorageService.validateAuthor(link, Link.class, user, "You're not the author of the link.");
        }

        return link;
    }
}
