package br.com.harvest.onboardexperience.infra.storage.services;

import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.infra.storage.dtos.UploadForm;
import br.com.harvest.onboardexperience.infra.storage.entities.Link;
import br.com.harvest.onboardexperience.infra.storage.interfaces.StorageService;
import br.com.harvest.onboardexperience.infra.storage.mappers.LinkMapper;
import br.com.harvest.onboardexperience.infra.storage.repositories.LinkRepository;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.services.ClientService;
import br.com.harvest.onboardexperience.services.TenantService;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LinkStorageService implements StorageService {

    @Autowired
    private LinkRepository repository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ClientService clientService;

    @Override
    public void save(@NonNull UploadForm form, String token) {
        validate(form);

        Client client = tenantService.fetchClientByTenantFromToken(token);

        Link link = Link.builder().authorizedClients(fetchClients(form.getAuthorizedClients(), client)).build();

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

        return repository.findAllByAuthorizedClients(client, pageable).map(LinkMapper.INSTANCE::toLinkSimpleDto);
    }

    private List<Client> fetchClients(List<Long> clientsId, Client author){
        List<Client> clients = new ArrayList<>() {{ add(author); }};

        if(ObjectUtils.isNotEmpty(clientsId)){
            for(Long clientId : clientsId){
                if(clientId.equals(author.getId())) continue;
                Client client = ClientMapper.INSTANCE.toEntity(clientService.findById(clientId));
                clients.add(client);
            }
        }

        return clients;
    }
}
