package br.com.harvest.onboardexperience.usecases;

import br.com.harvest.onboardexperience.domain.dtos.CoinDto;
import br.com.harvest.onboardexperience.domain.dtos.UserCoinDto;
import br.com.harvest.onboardexperience.domain.entities.Coin;
import br.com.harvest.onboardexperience.domain.entities.User;
import br.com.harvest.onboardexperience.domain.entities.UserCoin;
import br.com.harvest.onboardexperience.domain.entities.UserCoinKey;
import br.com.harvest.onboardexperience.domain.enumerators.CoinOperation;
import br.com.harvest.onboardexperience.domain.exceptions.InsufficientCoinException;
import br.com.harvest.onboardexperience.repositories.UserCoinRepository;
import br.com.harvest.onboardexperience.services.FetchService;
import br.com.harvest.onboardexperience.usecases.forms.UserCoinForm;
import br.com.harvest.onboardexperience.utils.JwtTokenUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserCoinUseCase {

    @Autowired
    private FetchService fetchService;

    @Autowired
    private UserCoinRepository repository;

    @Autowired
    private JwtTokenUtils tokenUtils;

    public void addCoinToUser(@NonNull UserCoinForm form){
        executeOperationAndSave(form, CoinOperation.ADD);
    }

    public void subtractCoinFromUser(@NonNull UserCoinForm form){
        executeOperationAndSave(form, CoinOperation.SUBTRACT);
    }

    public List<UserCoinDto> getAllCoinAmountFromUser(@NonNull String token){
        User user = fetchService.fetchUser(tokenUtils.getUserId(token));
        return repository.findByUser(user).stream().map(UserCoinUseCase::convertToUserCoinDto).collect(Collectors.toList());
    }

    private void executeOperationAndSave(@NonNull UserCoinForm form, @NonNull CoinOperation coinOperation){
        User user = fetchService.fetchUser(form.getUserId());
        Coin coin = fetchService.fetchCoin(form.getCoinId());

        Optional<UserCoin> userCoinOptional = getSelectedCoin(user, coin);

        UserCoin userCoin = executeOperation(form, userCoinOptional, coinOperation);

        repository.save(userCoin);
    }

    private static UserCoinDto convertToUserCoinDto(UserCoin userCoin){
        return UserCoinDto.builder()
                .coinName(userCoin.getCoin().getName())
                .id(userCoin.getCoin().getId())
                .amount(userCoin.getAmount())
                .build();
    }

    private UserCoin executeOperation(@NonNull UserCoinForm form, Optional<UserCoin> userCoinOptional,
                         @NonNull CoinOperation coinOperation){
        switch (coinOperation) {
            case ADD: {
                if(checkIfUserHaveSelectedCoin(userCoinOptional)){
                    UserCoin userCoin = userCoinOptional.get();
                    userCoin.setAmount(userCoin.getAmount().add(form.getAmount()));
                    return userCoin;
                }
                return convertFormToUserCoin(form);
            }
            case SUBTRACT: {
                if(checkIfUserHaveSelectedCoin(userCoinOptional)){
                    UserCoin userCoin = userCoinOptional.get();
                    validateSubtractAmount(form.getAmount(), userCoin.getAmount());
                    userCoin.setAmount(userCoin.getAmount().subtract(form.getAmount()));
                    return userCoin;
                }
                throwInsufficientCoinException();
            }
        }
        return null;
    }

    private UserCoin convertFormToUserCoin(@NonNull UserCoinForm form){
        return convertFormToUserCoin(fetchService.fetchUser(form.getUserId()), fetchService.fetchCoin(form.getCoinId()), form.getAmount());
    }

    private UserCoinKey generateUserCoinKey(@NonNull User user, @NonNull Coin coin){
        return UserCoinKey.builder()
                .idCoin(coin.getId())
                .idUser(user.getId())
                .build();
    }

    private UserCoin convertFormToUserCoin(@NonNull User user, @NonNull Coin coin, @NonNull BigInteger amount){
        return UserCoin.builder()
                .user(user)
                .coin(coin)
                .amount(amount)
                .id(generateUserCoinKey(user, coin))
                .build();
    }
    
    private Optional<UserCoin> getSelectedCoin(@NonNull User user, @NonNull Coin coin){
        return user.getCoins().stream().filter(userCoin -> userCoin.getCoin().getId()
                .equals(coin.getId())).findAny();
    }

    private Boolean checkIfUserHaveSelectedCoin(Optional<UserCoin> coin){
        return coin.isEmpty() ? false : true;
    }

    private void validateSubtractAmount(@NonNull BigInteger amountToSubtract, @NonNull BigInteger userAmount){
        if(userAmount.compareTo(amountToSubtract) == -1){
            throwInsufficientCoinException();
        }
    }

    private void throwInsufficientCoinException(){
        throw new InsufficientCoinException("Error to subtract coins from user.");
    }
}
