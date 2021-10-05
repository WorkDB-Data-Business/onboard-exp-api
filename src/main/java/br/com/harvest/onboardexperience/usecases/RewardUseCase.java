package br.com.harvest.onboardexperience.usecases;

import br.com.harvest.onboardexperience.domain.dtos.RewardDto;
import br.com.harvest.onboardexperience.domain.dtos.forms.RewardForm;
import br.com.harvest.onboardexperience.domain.entities.RewardPurchase;
import br.com.harvest.onboardexperience.repositories.RewardPurchaseRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.usecases.forms.RewardPurchaseForm;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardUseCase {

    @Autowired
    private FetchService fetchService;

    @Autowired
    private RewardPurchaseRepository rewardPurchaseRepository;

    public void purchaseReward(@NonNull RewardPurchaseForm form, @NonNull String token){

        RewardPurchase rewardPurchase = convertFormToRewardDto(form, token);

    }

    private RewardPurchase convertFormToRewardDto(@NonNull RewardPurchaseForm form, @NonNull String token){
        return RewardPurchase.builder()
                .user(fetchService.fetchUser(form.getUserId(), token))
                .haveConsumed(false)
                .reward(fetchService.fetchReward(form.getRewardId()))
                .build();
    }

}
