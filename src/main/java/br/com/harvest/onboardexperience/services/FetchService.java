package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchService {

    @Autowired
    private CompanyRoleService companyRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TenantService tenantService;

    public List<CompanyRoleDto> fetchCompanyRoles(List<Long> companyRolesId, String token){
        List<CompanyRoleDto> companyRoles = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(companyRolesId)){
            for(Long companyRoleId : companyRolesId){
                CompanyRoleDto companyRole = companyRoleService.findByIdAndTenant(companyRoleId, token);
                companyRoles.add(companyRole);
            }
        }
        return companyRoles;
    }

    public List<UserDto> fetchUsers(List<Long> usersId, String token){
        List<UserDto> users = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(usersId)){
            for(Long userId : usersId){
                UserDto user = userService.findByIdAndTenant(userId, token);
                users.add(user);
            }
        }
        return users;
    }

    public List<Client> fetchClients(List<Long> clientsId, Client author) {
        List<Client> clients = new ArrayList<>() {{
            add(author);
        }};

        if (ObjectUtils.isNotEmpty(clientsId)) {
            for (Long clientId : clientsId) {
                if (clientId.equals(author.getId())) continue;
                Client client = ClientMapper.INSTANCE.toEntity(clientService.findById(clientId));
                clients.add(client);
            }
        }

        return clients;
    }

    public List<Client> fetchClients(List<Long> clientsId, String token) {
        Client client = tenantService.fetchClientByTenantFromToken(token);
        return fetchClients(clientsId, client);
    }

    public List<Client> fetchClients(List<Long> clientsId) {
        List<Client> clients = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(clientsId)) {
            for (Long clientId : clientsId) {
                Client client = ClientMapper.INSTANCE.toEntity(clientService.findById(clientId));
                clients.add(client);
            }
        }

        return clients;
    }
}
