package br.com.harvest.onboardexperience.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.harvest.onboardexperience.domain.dtos.ClientDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.exceptions.ClientNotFoundException;
import br.com.harvest.onboardexperience.domain.factories.ExceptionMessageFactory;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.repositories.ClientRepository;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;

@Service
public class TenantService {

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private ClientRepository clientRepository;

    public Client fetchClientByTenantFromToken(String token) {
        String tenant = jwtUtils.getUserTenant(token);
        return clientRepository.findByTenantContainingIgnoreCase(tenant).orElseThrow(() -> new ClientNotFoundException(
                ExceptionMessageFactory.createNotFoundMessage("client", "tenant", tenant)));
    }

    public ClientDto fetchClientDtoByTenantFromToken(String token) {
        String tenant = jwtUtils.getUserTenant(token);
        return ClientMapper.INSTANCE.toDto(clientRepository.findByTenantContainingIgnoreCase(tenant).orElseThrow(() -> new ClientNotFoundException(
                ExceptionMessageFactory.createNotFoundMessage("client", "tenant", tenant))));
    }

}
