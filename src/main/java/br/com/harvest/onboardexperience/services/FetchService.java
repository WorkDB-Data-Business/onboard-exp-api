package br.com.harvest.onboardexperience.services;

import br.com.harvest.onboardexperience.domain.dtos.CompanyRoleDto;
import br.com.harvest.onboardexperience.domain.dtos.UserDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.mappers.ClientMapper;
import br.com.harvest.onboardexperience.mappers.CoinMapper;
import br.com.harvest.onboardexperience.mappers.CompanyRoleMapper;
import br.com.harvest.onboardexperience.mappers.UserMapper;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private StageService stageService;

    private final Long HARVEST_CLIENT = 1L;

    public List<CompanyRoleDto> fetchCompanyRoles(List<Long> companyRolesId, String token){
        if(ObjectUtils.isNotEmpty(companyRolesId)){
            return companyRolesId.stream().map(id -> companyRoleService.findByIdAndTenant(id, token))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<UserDto> fetchUsers(List<Long> usersId, String token){
        if(ObjectUtils.isNotEmpty(usersId)){
            return usersId.stream().map(id -> userService.findUserByIdAndTenant(id, token))
                    .map(UserMapper.INSTANCE::toDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<User> fetchUsers(List<Long> usersId){
        if(ObjectUtils.isNotEmpty(usersId)){
            return usersId.stream().map(userService::findUserById).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Client fetchClientById(@NonNull Long id){
        return ClientMapper.INSTANCE.toEntity(clientService.findById(id));
    }

    public Client fetchHavestClient(){
        return fetchClientById(HARVEST_CLIENT);
    }

    public List<Client> fetchClients(List<Long> clientsId) {
        if(ObjectUtils.isNotEmpty(clientsId)){
            return clientsId.stream().map(clientService::findById)
                    .map(ClientMapper.INSTANCE::toEntity).collect(Collectors.toList());
        }
        return Collections.emptyList();
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
    public Stage fetchStage(@NonNull Long id, @NonNull String token){
        return stageService.findRewardByIdAndTenant(id, token);
    }

}
