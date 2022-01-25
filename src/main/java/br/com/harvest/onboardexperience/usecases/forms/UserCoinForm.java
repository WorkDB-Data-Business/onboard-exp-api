package br.com.harvest.onboardexperience.usecases.forms;

import br.com.harvest.onboardexperience.domain.enumerators.CoinOperation;
import br.com.harvest.onboardexperience.utils.ValidationUtils;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserCoinForm {

    @NotNull
    private Long userId;

    @NotNull
    private Long coinId;

    @NotNull
    private Long StageId;

    @NotNull
    @Min(ValidationUtils.MIN_COIN)
    private BigInteger amount;

    private CoinOperation operation;

}
