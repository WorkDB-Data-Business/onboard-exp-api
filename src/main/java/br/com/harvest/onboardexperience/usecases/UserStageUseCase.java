package br.com.harvest.onboardexperience.usecases;

import br.com.harvest.onboardexperience.domain.dtos.StageDto;
import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.exceptions.InsufficientCoinException;
import br.com.harvest.onboardexperience.mappers.StageMapper;
import br.com.harvest.onboardexperience.repositories.StageRepository;
import br.com.harvest.onboardexperience.repositories.UserCoinRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.services.TenantService;
import br.com.harvest.onboardexperience.usecases.forms.UserCoinForm;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserStageUseCase {

    @Autowired
    private FetchService fetchService;

    @Autowired
    private StageRepository repository;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCoinRepository userCoinRepository;

    // Retorna com todas as etapas disponiveis
    public List<StageDto> findAllStagesAvailables(@NonNull StageDto dto, MultipartFile file, String token) {
        String tenant = jwtUtils.getUserTenant(token);

        List<Stage> stages = repository.findAllByIsAvailableAndClient_Tenant(true, tenant);

        return stages.stream().map(StageMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public void completeStage(UserCoinForm form, String token) {

        Stage stage = fetchService.fetchStage(form.getStageId(), token);
        User user = fetchService.fetchUser(form.getUserId(), token);

        UserCoin coin = getSelectedCoin(user, stage.getCoin());

        gainCoin(stage, coin, user);
    }

    private UserCoin getSelectedCoin(@NonNull User user, @NonNull Coin stageCoin){

        Optional<UserCoin> coin = user.getCoins().stream().filter(userCoin -> userCoin.getCoin().getId().equals(stageCoin.getId())).findAny();

        return coin.orElseGet(() -> registerGainCoin(stageCoin, user));
    }

    private void gainCoin(Stage stage, UserCoin userCoin, @NonNull User user){
        userCoin.setAmount(userCoin.getAmount().add(stage.getAmountCoins()));
        addCoinToUser(userCoin, user);
    }

    private void addCoinToUser(UserCoin coin, @NonNull User user){
        user.getCoins().add(coin);
        userRepository.save(user);
    }
    private UserCoin registerGainCoin(Coin coin, @NonNull User user){
        UserCoin userCoin = createRegisterGain(coin, user);
        return userCoinRepository.save(userCoin);
    }
    private UserCoin createRegisterGain(Coin coin, @NonNull User user){
        return UserCoin.builder()
                .user(user)
                .amount(new BigInteger ("0"))
                .coin(coin)
                .build();
    }
}
