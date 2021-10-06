package br.com.harvest.onboardexperience.usecases;

import br.com.harvest.onboardexperience.domain.entities.*;
import br.com.harvest.onboardexperience.domain.exceptions.InsufficientCoinException;
import br.com.harvest.onboardexperience.repositories.RewardPurchaseRepository;
import br.com.harvest.onboardexperience.repositories.UserCoinRepository;
import br.com.harvest.onboardexperience.repositories.UserRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.usecases.forms.RewardPurchaseForm;
import br.com.harvest.onboardexperience.utils.GenericUtils;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

@Service
public class UserRewardUseCase {

    @Autowired
    private FetchService fetchService;

    @Autowired
    private RewardPurchaseRepository rewardPurchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCoinRepository userCoinRepository;

    public void purchaseReward(@NonNull RewardPurchaseForm form, @NonNull final String token){

        Reward reward = fetchService.fetchReward(form.getRewardId(), token);
        User user = fetchService.fetchUser(form.getUserId(), token);

        UserCoin userCoin = getSelectedCoin(user.getCoins(), reward.getCoin());

        purchase(reward, userCoin, user);

    }

    private void purchase(@NonNull Reward reward, @NonNull UserCoin userCoin, @NonNull User user){
        validateSubtractAmount(reward.getPrice(), userCoin.getAmount());
        subtractAmountFromUserCoin(reward, userCoin);
        addRewardToUser(reward, user);
    }

    private void addRewardToUser(@NonNull Reward reward, @NonNull User user){
        user.getRewards().add(registerPurchase(reward, user));
        userRepository.save(user);
    }

    private RewardPurchase registerPurchase(@NonNull Reward reward, @NonNull User user){
        RewardPurchase rewardPurchase = createRegisterPurchase(reward, user);
        return rewardPurchaseRepository.save(rewardPurchase);
    }

    private void subtractAmountFromUserCoin(@NonNull Reward reward, @NonNull UserCoin userCoin){
        userCoin.setAmount(userCoin.getAmount().subtract(reward.getPrice()));
        userCoinRepository.save(userCoin);
    }

    private UserCoin getSelectedCoin(@NonNull Set<UserCoin> userCoins, @NonNull Coin rewardCoin){
        if(ObjectUtils.isEmpty(userCoins)){
            throw new InsufficientCoinException("The user doesn't have any coin.");
        }

        Optional<UserCoin> coin = userCoins.stream().filter(userCoin -> userCoin.getCoin().getId().equals(rewardCoin.getId())).findAny();

        validateCoin(coin);

        return coin.get();
    }

    private void validateCoin(Optional<UserCoin> coin){
        if(!GenericUtils.checkIfOptionalHaveValue(coin)){
            throw new InsufficientCoinException("The user doesn't have the same coin of the reward.");
        }
    }


    private RewardPurchase createRegisterPurchase(@NonNull Reward reward, @NonNull User user){
        return RewardPurchase.builder()
                .user(user)
                .haveConsumed(false)
                .reward(reward)
                .build();
    }

    private void validateSubtractAmount(@NonNull BigInteger amountToSubtract, @NonNull BigInteger userAmount){
        if(userAmount.compareTo(amountToSubtract) == -1){
            throw new InsufficientCoinException("The user doesn't have sufficient coin to purchase this reward.");
        }
    }

}
