package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.entities.Client;
import br.com.harvest.onboardexperience.domain.entities.Coin;
import br.com.harvest.onboardexperience.domain.entities.Reward;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.mappers.CoinMapper;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CoinService coinService;

    @Autowired
    private RewardService rewardService;

    private final Long HARVEST_CLIENT = 1L;

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
                UserDto user = userService.findUserDtoByIdAndTenant(userId, token);
                users.add(user);
            }
        }
        return users;
    }

    public List<User> fetchUsers(List<Long> usersId){
        List<User> users = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(usersId)){
            for(Long userId : usersId){
                User user = userService.findUserById(userId);
                users.add(user);
            }
        }
        return users;
    }

    public Client fetchClientById(@NonNull Long id){
        return ClientMapper.INSTANCE.toEntity(clientService.findById(id));
    }

    public Client fetchHavestClient(){
        return fetchClientById(HARVEST_CLIENT);
    }

    public List<Client> fetchClients(List<Long> clientsId) {
        return clientsId.stream().map(clientService::findById)
                .map(ClientMapper.INSTANCE::toEntity).collect(Collectors.toList());
    }

    public User fetchUser(@NonNull Long id, @NonNull String token){
        return userService.findUserByIdAndTenant(id, token);
    }

    public User fetchUser(@NonNull Long id){
        return userService.findUserById(id);
    }

    public Coin fetchCoin(@NonNull Long id, @NonNull String token){
        return CoinMapper.INSTANCE.toEntity(coinService.findByIdAndTenant(id, token));
    }

    public Reward fetchReward(@NonNull Long id, @NonNull String token){
        return rewardService.findRewardByIdAndTenant(id, token);
    }
}
